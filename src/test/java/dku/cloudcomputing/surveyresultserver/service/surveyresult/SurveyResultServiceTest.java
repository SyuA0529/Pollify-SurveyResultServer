package dku.cloudcomputing.surveyresultserver.service.surveyresult;

import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import dku.cloudcomputing.surveyresultserver.entity.survey.*;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.MultipleChoiceSurveyResult;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SubjectiveSurveyResult;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SurveyResult;
import dku.cloudcomputing.surveyresultserver.exception.ClientOccurException;
import dku.cloudcomputing.surveyresultserver.repository.member.MemberRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.MultipleChoiceOptionRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyDetailRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyRepository;
import dku.cloudcomputing.surveyresultserver.repository.surveyresult.SurveyResultRepository;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.dto.CreateSurveyResultDto;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class SurveyResultServiceTest {
    @Autowired
    private SurveyResultService surveyResultService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyDetailRepository detailRepository;
    @Autowired
    private MultipleChoiceOptionRepository optionRepository;
    @Autowired
    private SurveyResultRepository resultRepository;

    @Test
    @DisplayName("설문 결과 저장 테스트")
    void saveSurveyResult() {
        Member savedMember = memberRepository.save(new Member(1L, "test@test", "test", "tset"));
        Survey savedSurvey = surveyRepository.save(new Survey(1L, savedMember, "test", LocalDate.now(), 30, true));
        SubjectiveSurveyDetail savedSubjectiveDetail = detailRepository.save(new SubjectiveSurveyDetail(1L, savedSurvey, "test"));
        MultipleChoiceSurveyDetail savedMultipleChoiceDetail = detailRepository.save(new MultipleChoiceSurveyDetail(2L, savedSurvey, "test"));
        for (int i = 0; i < 5; i++)
            optionRepository.save(new MultipleChoiceOption((long) (i + 1), savedMultipleChoiceDetail, "option"));

        List<CreateSurveyResultDto> surveyResultDtos = new ArrayList<>();
        surveyResultDtos.add(new CreateSurveyResultDto(1L, SurveyDetailType.SUBJECTIVE, "testContent", null));
        surveyResultDtos.add(new CreateSurveyResultDto(2L, SurveyDetailType.MULTIPLE_CHOICE, null, 3L));
        surveyResultService.saveSurveyResult(1L, surveyResultDtos);

        List<SurveyResult> surveyResults = resultRepository.findAll();
        assertThat(surveyResults.size()).isEqualTo(2);

        List<SurveyResult> subjectiveResults = surveyResults.stream().filter(SubjectiveSurveyResult.class::isInstance).toList();
        assertThat(subjectiveResults.size()).isEqualTo(1);
        SubjectiveSurveyResult subjectiveResult = (SubjectiveSurveyResult) subjectiveResults.get(0);
        assertThat(subjectiveResult.getContent()).isEqualTo("testContent");
        assertThat(subjectiveResult.getSurveyDetail().getId()).isEqualTo(savedSubjectiveDetail.getId());

        List<SurveyResult> multipleChoiceResults = surveyResults.stream().filter(MultipleChoiceSurveyResult.class::isInstance).toList();
        assertThat(multipleChoiceResults.size()).isEqualTo(1);
        MultipleChoiceSurveyResult multipleChoiceResult = (MultipleChoiceSurveyResult) multipleChoiceResults.get(0);
        assertThat(multipleChoiceResult.getOption().getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("설문에 포함되지 않는 설문 세부 항목 포함시 저장 실패")
    void saveSurveyResultFailWhenSurveyDetailNotInSurvey() {
        Member savedMember = memberRepository.save(new Member(1L, "test@test", "test", "tset"));
        Survey savedSurvey = surveyRepository.save(new Survey(1L, savedMember, "test", LocalDate.now(), 30, true));
        Survey savedSurvey2 = surveyRepository.save(new Survey(2L, savedMember, "test", LocalDate.now(), 30, true));

        SubjectiveSurveyDetail savedSubjectiveDetail = detailRepository.save(new SubjectiveSurveyDetail(1L, savedSurvey, "test"));
        MultipleChoiceSurveyDetail savedMultipleChoiceDetail = detailRepository.save(new MultipleChoiceSurveyDetail(2L, savedSurvey, "test"));

        detailRepository.save(new SubjectiveSurveyDetail(3L, savedSurvey2, "test"));

        for (int i = 0; i < 5; i++)
            optionRepository.save(new MultipleChoiceOption((long) (i + 1), savedMultipleChoiceDetail, "option"));

        List<CreateSurveyResultDto> surveyResultDtos = new ArrayList<>();
        surveyResultDtos.add(new CreateSurveyResultDto(3L, SurveyDetailType.SUBJECTIVE, "testContent", null));
        surveyResultDtos.add(new CreateSurveyResultDto(2L, SurveyDetailType.MULTIPLE_CHOICE, null, 3L));

        assertThatThrownBy(() -> surveyResultService.saveSurveyResult(1L, surveyResultDtos))
                .isInstanceOf(ClientOccurException.class);
    }

    @Test
    @DisplayName("설문에 포함되지 않는 객관식(옵션) 항목 포함시 저장 실패")
    void saveSurveyResultFailWhenOptionNotInSurvey() {
        Member savedMember = memberRepository.save(new Member(1L, "test@test", "test", "tset"));
        Survey savedSurvey1 = surveyRepository.save(new Survey(1L, savedMember, "test", LocalDate.now(), 30, true));
        Survey savedSurvey2 = surveyRepository.save(new Survey(2L, savedMember, "test", LocalDate.now(), 30, true));

        detailRepository.save(new SubjectiveSurveyDetail(1L, savedSurvey1, "test"));
        MultipleChoiceSurveyDetail savedMultipleChoiceDetail1 = detailRepository.save(new MultipleChoiceSurveyDetail(2L, savedSurvey1, "test"));
        MultipleChoiceSurveyDetail savedMultipleChoiceDetail2 = detailRepository.save(new MultipleChoiceSurveyDetail(3L, savedSurvey2, "test"));


        for (int i = 0; i < 5; i++) {
            optionRepository.save(new MultipleChoiceOption((long) (i + 1), savedMultipleChoiceDetail1, "option"));
            optionRepository.save(new MultipleChoiceOption((long) (i + 1 + 5), savedMultipleChoiceDetail2, "option"));
        }

        List<CreateSurveyResultDto> surveyResultDtos = new ArrayList<>();
        surveyResultDtos.add(new CreateSurveyResultDto(2L, SurveyDetailType.MULTIPLE_CHOICE, null, 3L));
        surveyResultDtos.add(new CreateSurveyResultDto(2L, SurveyDetailType.MULTIPLE_CHOICE, null, 8L));

        assertThatThrownBy(() -> surveyResultService.saveSurveyResult(1L, surveyResultDtos))
                .isInstanceOf(ClientOccurException.class);
    }
}