package dku.cloudcomputing.surveyresultserver.repository.surveyresult;


import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import dku.cloudcomputing.surveyresultserver.entity.survey.*;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.MultipleChoiceSurveyResult;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SubjectiveSurveyResult;
import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SurveyResult;
import dku.cloudcomputing.surveyresultserver.repository.member.MemberRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.MultipleChoiceOptionRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyDetailRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
class SurveyResultRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SurveyRepository surveyRepository;
    @Autowired
    SurveyDetailRepository surveyDetailRepository;
    @Autowired
    MultipleChoiceOptionRepository multipleChoiceOptionRepository;
    @Autowired
    SurveyResultRepository surveyResultRepository;

    @Test
    @DisplayName("주관식 설문 결과 저장")
    void saveSubjectiveSurveyResult() {
        //given
        Member member = new Member(1L, "test@test", "test", "test");
        memberRepository.saveAndFlush(member);
        Survey survey = new Survey(1L, member, "test", LocalDate.now(), 30, true);
        surveyRepository.saveAndFlush(survey);
        SurveyDetail subjectiveSD = new SubjectiveSurveyDetail(1L, survey, "question");
        surveyDetailRepository.saveAndFlush(subjectiveSD);

        List<SurveyResult> subjectives = subjectiveSD.getSurveyResults();
        for (int i = 0; i < 5; i++)
            subjectives.add(new SubjectiveSurveyResult(subjectiveSD, "content" + i));

        //when
        surveyResultRepository.saveAllAndFlush(subjectives);

        //then
        List<SurveyResult> surveyResults = surveyResultRepository.findAll();
        assertThat(surveyResults.size()).isEqualTo(subjectives.size());
        for (int i = 0; i < 5; i++) {
            SurveyResult surveyResult = surveyResults.get(i);
            assertThat(surveyResult.getSurveyDetail()).isInstanceOf(SubjectiveSurveyDetail.class);
            assertThat(surveyResult).isInstanceOf(SubjectiveSurveyResult.class);
            assertThat(((SubjectiveSurveyResult) surveyResult).getContent())
                    .isEqualTo("content" + i);
        }
    }

    @Test
    @DisplayName("객관식 설문 결과 저장")
    void saveMultipleChoiceSurveyResult() {
        //given
        Member member = new Member(1L, "test@test", "test", "test");
        memberRepository.saveAndFlush(member);
        Survey survey = new Survey(1L, member, "test", LocalDate.now(), 30, true);
        surveyRepository.saveAndFlush(survey);
        MultipleChoiceSurveyDetail multipleChoiceSD = new MultipleChoiceSurveyDetail(1L, survey, "question");
        surveyDetailRepository.saveAndFlush(multipleChoiceSD);

        List<MultipleChoiceOption> multipleChoiceOptions =
                multipleChoiceSD.getMultipleChoiceOptions();
        for (int i = 0; i < 5; i++)
            multipleChoiceOptions.add(new MultipleChoiceOption((long) (i + 1), multipleChoiceSD, "option" + i));
        multipleChoiceOptionRepository.saveAllAndFlush(multipleChoiceOptions);

        //when
        List<SurveyResult> surveyResults = multipleChoiceSD.getSurveyResults();
        for (int i = 0; i < 10; i++)
            surveyResults.add(new MultipleChoiceSurveyResult(multipleChoiceSD, multipleChoiceOptions.get(i % 5)));
        surveyResultRepository.saveAllAndFlush(surveyResults);

        //then
        List<SurveyResult> results = surveyResultRepository.findAll();
        assertThat(results.size()).isEqualTo(10);
        for (int i = 0; i < 10; i++) {
            SurveyResult surveyResult = results.get(i);
            assertThat(surveyResult).isInstanceOf(MultipleChoiceSurveyResult.class);
            assertThat(surveyResult.getSurveyDetail()).isInstanceOf(MultipleChoiceSurveyDetail.class);
            assertThat(((MultipleChoiceSurveyResult) surveyResult).getOption().getId())
                    .isEqualTo(multipleChoiceOptions.get(i % 5).getId());
        }
    }
}