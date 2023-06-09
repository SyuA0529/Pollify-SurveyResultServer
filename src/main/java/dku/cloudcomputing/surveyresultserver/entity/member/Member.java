package dku.cloudcomputing.surveyresultserver.entity.member;

import dku.cloudcomputing.surveyresultserver.entity.survey.Survey;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    private Long id;
    private String email;
    private String password;
    private String nickname;

    @OneToMany(mappedBy = "member")
    private final List<Survey> surveys = new ArrayList<>();

    public Member(Long id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
