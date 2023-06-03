package dku.cloudcomputing.surveyresultserver.repository.survey.query;

import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SurveyQueryRepository extends JpaRepository<Survey, Long> {

    @Query("select distinct s from Survey s join fetch s.surveyDetails where s.id = :surveyId")
    Optional<Survey> findDetailSurveyById(@Param("surveyId") Long surveyId);
}
