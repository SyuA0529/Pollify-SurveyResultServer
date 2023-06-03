package dku.cloudcomputing.surveyresultserver.repository.surveyresult.query;

import dku.cloudcomputing.surveyresultserver.entity.surveyresult.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResultQueryRepository extends JpaRepository<SurveyResult, Long> {
    @Query("select sr from SurveyResult sr where sr.surveyDetail.id in :detailIdList")
    List<SurveyResult> findByDetailIdList(@Param("detailIdList") List<Long> detailIdList);
}
