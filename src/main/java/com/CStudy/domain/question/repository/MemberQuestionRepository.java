package com.CStudy.domain.question.repository;

import com.CStudy.domain.member.entity.Member;
import com.CStudy.domain.question.entity.MemberQuestion;
import com.CStudy.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberQuestionRepository extends JpaRepository<MemberQuestion, Long> {



    @Query("SELECT CASE WHEN COUNT(mq) > 0 THEN true ELSE false END " +
            "FROM MemberQuestion mq " +
            "WHERE mq.member.id = :memberId " +
            "AND mq.question.id = :questionId " +
            "AND mq.success = true")
    boolean existsByMemberAndQuestionAndSuccess(@Param("memberId") Long memberId,
                                                @Param("questionId") Long questionId);
    Optional<MemberQuestion> findByMemberAndQuestion(Member member, Question question);

}
