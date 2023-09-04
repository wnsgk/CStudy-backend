package com.CStudy.domain.question.entity;

import com.CStudy.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class MemberQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_success")
    private Boolean success;

    private LocalDateTime solveTime;
    private Integer choice;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "question_id")
    private Question question;

    @Builder
    public MemberQuestion(
            Boolean success,
            Integer choice,
            LocalDateTime solveTime,
            Member member,
            Question question
    ) {
        this.choice = choice;
        this.success = success;
        this.solveTime = solveTime;
        this.member = member;
        this.question = question;
    }

    public void changeAnswer(Boolean success, Integer choice, LocalDateTime solveTime){
        this.success = success;
        this.choice = choice;
        this.solveTime = solveTime;
    }
}
