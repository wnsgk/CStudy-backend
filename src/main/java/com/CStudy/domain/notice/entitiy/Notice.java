package com.CStudy.domain.notice.entitiy;

import com.CStudy.domain.member.entity.BaseEntity;
import com.CStudy.domain.member.entity.Member;
import com.CStudy.domain.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notice_content", nullable = false)
    private String content;

    @JoinColumn(name = "member_id")
    @ManyToOne(optional = false)
    private Member member;

    @JoinColumn(name = "question_id")
    @ManyToOne
    private Question question;

    private LocalDateTime noticeTime;

    @Builder
    public Notice(
            String content,
            Member member,
            Question question,
            LocalDateTime noticeTime
    ) {
        this.content = content;
        this.member = member;
        this.question = question;
        this.noticeTime = noticeTime;
    }

}
