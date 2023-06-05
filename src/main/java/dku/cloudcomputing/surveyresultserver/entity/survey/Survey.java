package dku.cloudcomputing.surveyresultserver.entity.survey;

import dku.cloudcomputing.surveyresultserver.entity.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Survey {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String name;
    private LocalDate startDate;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private boolean visibility;

    @OneToMany(mappedBy = "survey")
    private final List<SurveyDetail> surveyDetails = new ArrayList<>();

    public Survey(Long id, Member member, String name, LocalDate startDate, int duration, boolean visibility) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.startDate = startDate;
        this.duration = duration;
        this.visibility = visibility;

        member.getSurveys().add(this);
    }

    public void clearSurveyDetails() {
        surveyDetails.clear();
    }
}
