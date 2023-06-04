package dku.cloudcomputing.surveyresultserver.service.survey;

import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import dku.cloudcomputing.surveyresultserver.repository.member.MemberRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.MultipleChoiceOptionRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyDetailRepository;
import dku.cloudcomputing.surveyresultserver.repository.survey.SurveyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:31234", "port=31234" }
)
@Transactional
class KafkaSurveyServiceTest {
    @Autowired
    KafkaSurveyService kafkaSurveyService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SurveyRepository surveyRepository;
    @Autowired
    SurveyDetailRepository detailRepository;
    @Autowired
    MultipleChoiceOptionRepository optionRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("설문 DTO를 Entity로 변환")
    @Transactional
    void convertSurveyDtoToEntity() {
        memberRepository.save(new Member(1L, "test@test", "test", "test"));

        String json = "{\"id\":1,\"memberId\":1,\"name\":\"test\",\"startDate\":\"2023-06-03\",\"duration\":30,\"visibility\":true,\"surveyDetails\":[{\"id\":1,\"question\":\"question0\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":2,\"question\":\"question1\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":3,\"question\":\"question2\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":4,\"question\":\"question3\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":5,\"question\":\"question4\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":6,\"question\":\"question5\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":1,\"option\":\"test0\"},{\"id\":2,\"option\":\"test1\"},{\"id\":3,\"option\":\"test2\"},{\"id\":4,\"option\":\"test3\"},{\"id\":5,\"option\":\"test4\"}]},{\"id\":7,\"question\":\"question6\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":6,\"option\":\"test0\"},{\"id\":7,\"option\":\"test1\"},{\"id\":8,\"option\":\"test2\"},{\"id\":9,\"option\":\"test3\"},{\"id\":10,\"option\":\"test4\"}]},{\"id\":8,\"question\":\"question7\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":11,\"option\":\"test0\"},{\"id\":12,\"option\":\"test1\"},{\"id\":13,\"option\":\"test2\"},{\"id\":14,\"option\":\"test3\"},{\"id\":15,\"option\":\"test4\"}]},{\"id\":9,\"question\":\"question8\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":16,\"option\":\"test0\"},{\"id\":17,\"option\":\"test1\"},{\"id\":18,\"option\":\"test2\"},{\"id\":19,\"option\":\"test3\"},{\"id\":20,\"option\":\"test4\"}]},{\"id\":10,\"question\":\"question9\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":21,\"option\":\"test0\"},{\"id\":22,\"option\":\"test1\"},{\"id\":23,\"option\":\"test2\"},{\"id\":24,\"option\":\"test3\"},{\"id\":25,\"option\":\"test4\"}]}]}";
        kafkaSurveyService.saveSurvey(json);

        assertThat(surveyRepository.findAll().size()).isEqualTo(1);
        assertThat(detailRepository.findAll().size()).isEqualTo(10);
        assertThat(optionRepository.findAll().size()).isEqualTo(25);
    }

    @Test
    @DisplayName("설문 id로 설문 삭제")
    void deleteSurveyBySurveyId() {
        memberRepository.save(new Member(1L, "test@test", "test", "test"));
        String json = "{\"id\":1,\"memberId\":1,\"name\":\"test\",\"startDate\":\"2023-06-03\",\"duration\":30,\"visibility\":true,\"surveyDetails\":[{\"id\":1,\"question\":\"question0\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":2,\"question\":\"question1\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":3,\"question\":\"question2\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":4,\"question\":\"question3\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":5,\"question\":\"question4\",\"surveyDetailType\":\"SUBJECTIVE\",\"options\":[]},{\"id\":6,\"question\":\"question5\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":1,\"option\":\"test0\"},{\"id\":2,\"option\":\"test1\"},{\"id\":3,\"option\":\"test2\"},{\"id\":4,\"option\":\"test3\"},{\"id\":5,\"option\":\"test4\"}]},{\"id\":7,\"question\":\"question6\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":6,\"option\":\"test0\"},{\"id\":7,\"option\":\"test1\"},{\"id\":8,\"option\":\"test2\"},{\"id\":9,\"option\":\"test3\"},{\"id\":10,\"option\":\"test4\"}]},{\"id\":8,\"question\":\"question7\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":11,\"option\":\"test0\"},{\"id\":12,\"option\":\"test1\"},{\"id\":13,\"option\":\"test2\"},{\"id\":14,\"option\":\"test3\"},{\"id\":15,\"option\":\"test4\"}]},{\"id\":9,\"question\":\"question8\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":16,\"option\":\"test0\"},{\"id\":17,\"option\":\"test1\"},{\"id\":18,\"option\":\"test2\"},{\"id\":19,\"option\":\"test3\"},{\"id\":20,\"option\":\"test4\"}]},{\"id\":10,\"question\":\"question9\",\"surveyDetailType\":\"MULTIPLE_CHOICE\",\"options\":[{\"id\":21,\"option\":\"test0\"},{\"id\":22,\"option\":\"test1\"},{\"id\":23,\"option\":\"test2\"},{\"id\":24,\"option\":\"test3\"},{\"id\":25,\"option\":\"test4\"}]}]}";
        kafkaSurveyService.saveSurvey(json);

        kafkaSurveyService.deleteSurvey("1");
        finishTransaction();

        assertThat(surveyRepository.findAll().size()).isEqualTo(0);
        assertThat(detailRepository.findAll().size()).isEqualTo(0);
        assertThat(optionRepository.findAll().size()).isEqualTo(0);
    }

    private void finishTransaction() {
        em.flush();
        em.clear();
    }
}