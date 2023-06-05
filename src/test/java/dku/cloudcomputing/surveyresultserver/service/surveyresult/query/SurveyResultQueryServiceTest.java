package dku.cloudcomputing.surveyresultserver.service.surveyresult.query;

import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceOption;
import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.SubjectiveSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import dku.cloudcomputing.surveyresultserver.repository.member.MemberRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.MultipleChoiceOptionRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyDetailRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.query.dto.MultipleChoiceOptionQueryDto;
import dku.cloudcomputing.surveyresultserver.security.JwtAuthenticator;
import dku.cloudcomputing.surveyresultserver.service.survey.SurveyDetailType;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.SurveyResultService;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.dto.CreateSurveyResultDto;
import dku.cloudcomputing.surveyresultserver.service.surveyresult.query.dto.*;
import io.github.lazymockbean.annotation.LazyInjectMockBeans;
import io.github.lazymockbean.annotation.LazyMockBean;
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
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class SurveyResultQueryServiceTest {
    @LazyMockBean
    JwtAuthenticator jwtAuthenticator;

    @Autowired
    private SurveyResultService resultService;
    @LazyInjectMockBeans
    @Autowired
    private SurveyResultQueryService resultQueryService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyDetailRepository detailRepository;
    @Autowired
    private MultipleChoiceOptionRepository optionRepository;

    @Test
    @DisplayName("설문 결과 조회")
    void querySurveyResult() {
        //given
        Member savedMember = memberRepository.saveAndFlush(new Member(1L, "test@test", "test", "test"));
        when(jwtAuthenticator.getEmail("")).thenReturn("test@test");

        Survey savedSurvey = surveyRepository.saveAndFlush(new Survey(1L, savedMember, "test", LocalDate.now(), 30, true));
        SubjectiveSurveyDetail savedSubjectiveDetail = detailRepository.saveAndFlush(new SubjectiveSurveyDetail(1L, savedSurvey, "test"));
        MultipleChoiceSurveyDetail savedMultipleChoiceDetail = detailRepository.saveAndFlush(new MultipleChoiceSurveyDetail(2L, savedSurvey, "test"));
        for (int i = 0; i < 5; i++)
            optionRepository.saveAndFlush(new MultipleChoiceOption((long) (i + 1), savedMultipleChoiceDetail, "option"));

        List<CreateSurveyResultDto> surveyResultDtos = new ArrayList<>();
        surveyResultDtos.add(new CreateSurveyResultDto(1L, SurveyDetailType.SUBJECTIVE, "testContent", null));
        surveyResultDtos.add(new CreateSurveyResultDto(2L, SurveyDetailType.MULTIPLE_CHOICE, null, 3L));
        resultService.saveSurveyResult(1L, surveyResultDtos);

        //when
        SurveyQueryDto queryDto = resultQueryService.querySurveyResult("", savedSurvey.getId());

        //then
        assertThat(queryDto.getName()).isEqualTo(savedSurvey.getName());

        List<SurveyDetailQueryDto> surveyDetailQueryDtos = queryDto.getSurveyDetails();
        assertThat(surveyDetailQueryDtos.size()).isEqualTo(2);

        SurveyDetailQueryDto subjectiveDetailQueryDto = surveyDetailQueryDtos.get(0);
        assertThat(subjectiveDetailQueryDto.getDetailType()).isEqualTo(SurveyDetailType.SUBJECTIVE);
        assertThat(subjectiveDetailQueryDto.getQuestion()).isEqualTo("test");
        assertThat(subjectiveDetailQueryDto.getResultCount()).isEqualTo(1);

        SurveyDetailQueryDto multipleChoiceQueryDto = surveyDetailQueryDtos.get(1);
        assertThat(multipleChoiceQueryDto.getQuestion()).isEqualTo("test");
        assertThat(multipleChoiceQueryDto.getDetailType()).isEqualTo(SurveyDetailType.MULTIPLE_CHOICE);
        assertThat(multipleChoiceQueryDto.getResultCount()).isEqualTo(1);

        List<MultipleChoiceOptionQueryDto> dtoOptions =
                ((MultipleChoiceSurveyDetailQueryDto) multipleChoiceQueryDto).getOptions();
        for (int i = 0; i < 5; i++) assertThat(dtoOptions.get(i).getOption()).isEqualTo("option");

        List<SurveyResultQueryDto> subjectiveResultDtos = subjectiveDetailQueryDto.getResults();
        assertThat(subjectiveResultDtos.size()).isEqualTo(1);
        assertThat(subjectiveResultDtos.get(0)).isInstanceOf(SubjectiveResultQueryDto.class);
        assertThat(((SubjectiveResultQueryDto) subjectiveResultDtos.get(0)).getContent()).isEqualTo("testContent");

        List<SurveyResultQueryDto> multipleChoiceResultDtos = multipleChoiceQueryDto.getResults();
        assertThat(multipleChoiceResultDtos.size()).isEqualTo(1);
        assertThat(multipleChoiceResultDtos.get(0)).isInstanceOf(MultipleChoiceResultQueryDto.class);
        assertThat(((MultipleChoiceResultQueryDto) multipleChoiceResultDtos.get(0)).getSelectOptionId())
                .isEqualTo(3L);
    }
}