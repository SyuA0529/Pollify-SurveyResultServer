package dku.cloudcomputing.surveyresultserver.repository.surveyresult;

import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResultRepository extends JpaRepository<SurveyResult, Long> {

    @Query("delete from SurveyResult sr where sr.surveyDetail.id = :surveyDetailIds")
    void deleteBySurveyDetailIdList(@Param("surveyDetailIds") List<Long> surveyDetailIds);
}
