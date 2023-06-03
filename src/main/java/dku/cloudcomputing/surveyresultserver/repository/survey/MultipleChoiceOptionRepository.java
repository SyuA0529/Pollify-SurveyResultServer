package dku.cloudcomputing.surveyresultserver.repository.survey;

import dku.cloudcomputing.surveyresultserver.entity.survey.MultipleChoiceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultipleChoiceOptionRepository extends JpaRepository<MultipleChoiceOption, Long> {
    @Modifying
    @Query("delete from MultipleChoiceOption mco where mco.surveyDetail.id in :idList")
    void deleteByDetailIdList(@Param("idList") List<Long> idList);

    @Query("select mco from MultipleChoiceOption mco where mco.surveyDetail.id in :detailIds")
    List<MultipleChoiceOption> findBySurveyDetailIds(@Param("detailIds") List<Long> detailIds);
}
