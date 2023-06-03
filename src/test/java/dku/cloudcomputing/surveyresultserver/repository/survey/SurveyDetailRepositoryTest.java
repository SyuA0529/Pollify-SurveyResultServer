package dku.cloudcomputing.surveyresultserver.repository.survey;

import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import dku.cloudcomputing.surveyresultserver.entity.survey.SubjectiveSurveyDetail;
import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import dku.cloudcomputing.surveyresultserver.repository.member.MemberRepository;
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
class SurveyDetailRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyDetailRepository detailRepository;

    @Test
    @DisplayName("설문 id로 설문 세부 항목 id 목록 조회")
    void queryDetailIdsBySurveyId() {
        Member savedMember = memberRepository.save(new Member(1L, "test@test", "test", "test"));
        Survey survey = new Survey(1L, savedMember, "test", LocalDate.now(), 30, true);
        surveyRepository.saveAndFlush(survey);

        List<SurveyDetail> surveyDetails = survey.getSurveyDetails();
        for (int i = 0; i < 5; i++)
            surveyDetails.add(new SubjectiveSurveyDetail((long) (i + 1), survey, "test"));
        detailRepository.saveAllAndFlush(surveyDetails);

        List<Long> surveyDetailIds = detailRepository.findSurveyDetailIdsBySurveyId(survey.getId());
        for (int i = 0; i < 5; i++)
            assertThat(surveyDetailIds.get(i)).isEqualTo((i + 1));
    }
}