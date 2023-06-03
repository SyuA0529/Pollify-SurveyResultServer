package dku.cloudcomputing.surveyresultserver.service.survey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceOption;
import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import dku.cloudcomputing.surveyresultserver.exception.member.NoSuchMemberException;
import dku.cloudcomputing.surveyresultserver.repository.member.MemberRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.MultipleChoiceOptionRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyDetailRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyRepository;
import dku.cloudcomputing.surveyresultserver.repository.surveyresult.SurveyResultRepository;
import dku.cloudcomputing.surveyresultserver.service.survey.dto.KafkaSurveyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaSurveyService {
    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyDetailRepository detailRepository;
    private final MultipleChoiceOptionRepository optionRepository;
    private final SurveyResultRepository resultRepository;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${message.topic.saveMember}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void saveMember(@Payload String memberJson) {
        try {
            Member member = objectMapper.readValue(memberJson, Member.class);
            memberRepository.save(member);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${message.topic.saveSurvey}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void saveSurvey(@Payload String surveyJson) {
        try {
            KafkaSurveyDto kafkaSurveyDto = objectMapper.readValue(surveyJson, KafkaSurveyDto.class);
            Survey survey = kafkaSurveyDto.convertToSurveyEntity(
                    memberRepository.findById(kafkaSurveyDto.getMemberId())
                            .orElseThrow(NoSuchMemberException::new));

            List<SurveyDetail> surveyDetails = List.copyOf(survey.getSurveyDetails());
            List<MultipleChoiceOption> options = List.copyOf(getMultipleChoiceOptionList(survey));

            survey.clearSurveyDetails();
            surveyDetails.stream()
                    .filter(MultipleChoiceSurveyDetail.class::isInstance)
                    .forEach(e -> ((MultipleChoiceSurveyDetail) e).clearMultipleChoiceOptions());

            surveyRepository.save(survey);
            detailRepository.saveAll(surveyDetails);
            optionRepository.saveAll(options);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${message.topic.deleteSurvey}", groupId = "${spring.kafka.consumer.group-id}")
    @Transactional
    public void deleteSurvey(@Payload String surveyJson) {
        try {
            Long surveyId = objectMapper.readValue(surveyJson, Long.class);
            List<Long> detailIds = detailRepository.findSurveyDetailIdsBySurveyId(surveyId);
            resultRepository.deleteBySurveyDetailIdList(detailIds);
            optionRepository.deleteByDetailIdList(detailIds);
            detailRepository.deleteBySurveyId(surveyId);
            surveyRepository.deleteById(surveyId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private List<MultipleChoiceOption> getMultipleChoiceOptionList(Survey survey) {
        List<MultipleChoiceOption> multipleChoiceOptions = new ArrayList<>();
        for (SurveyDetail surveyDetail : survey.getSurveyDetails()) {
            if(surveyDetail instanceof MultipleChoiceSurveyDetail)
                multipleChoiceOptions.addAll(((MultipleChoiceSurveyDetail) surveyDetail).getMultipleChoiceOptions());
        }
        return multipleChoiceOptions;
    }
}
