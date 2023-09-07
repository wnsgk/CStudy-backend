package com.CStudy.domain.notice.repository;

import com.CStudy.domain.member.entity.Member;
import com.CStudy.domain.notice.entitiy.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findByMemberAndNoticeTimeBefore(Member member, LocalDateTime noticeTime, Pageable pageable);

    Integer countByMemberAndNoticeTimeBefore(Member member, LocalDateTime noticeTime);

    boolean existsByIdAndMember(Long id, Member member);


}
