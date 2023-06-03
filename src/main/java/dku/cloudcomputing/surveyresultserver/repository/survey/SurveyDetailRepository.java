package dku.cloudcomputing.surveyresultserver.repository.survey;

import dku.cloudcomputing.surveyresultserver.entity.survey.SurveyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyDetailRepository extends JpaRepository<SurveyDetail, Long> {
    @Query("select sd.id from SurveyDetail sd where sd.survey.id = :surveyId")
    List<Long> findSurveyDetailIdsBySurveyId(Long surveyId);

    @Modifying
    @Query("delete from SurveyDetail sd where sd.survey.id = :surveyId")
    void deleteBySurveyId(@Param("surveyId") Long surveyId);

    @Query("select sd from SurveyDetail sd join fetch Survey where sd.survey.id = :surveyId")
    List<SurveyDetail> findSurveyDetailsBySurveyId(@Param("surveyId") Long surveyId);
}
