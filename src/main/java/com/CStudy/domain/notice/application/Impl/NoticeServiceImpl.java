package com.CStudy.domain.notice.application.Impl;

import com.CStudy.domain.member.entity.Member;
import com.CStudy.domain.member.repository.MemberRepository;
import com.CStudy.domain.notice.application.NoticeService;
import com.CStudy.domain.notice.dto.request.NoticeRequest;
import com.CStudy.domain.notice.dto.request.NoticeSearchDto;
import com.CStudy.domain.notice.dto.response.NoticeResponseDto;
import com.CStudy.domain.notice.entitiy.Notice;
import com.CStudy.domain.notice.repository.NoticeRepository;
import com.CStudy.domain.question.entity.Question;
import com.CStudy.domain.question.repository.QuestionRepository;
import com.CStudy.global.exception.member.NotFoundMemberId;
import com.CStudy.global.exception.notice.NotFoundNoticeId;
import com.CStudy.global.exception.notice.NotMatchAdminIpException;
import com.CStudy.global.exception.question.NotFoundQuestionId;
import com.CStudy.global.util.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final MemberRepository memberRepository;
    private final NoticeRepository noticeRepository;
    private final QuestionRepository questionRepository;


    @Override
    @Transactional
    public Long saveNotice(NoticeRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new NotFoundQuestionId(request.getQuestionId()));

        Notice notice = noticeRepository.save(Notice.builder()
                .content(request.getContent())
                .question(question)
                .member(member)
                .noticeTime(request.getNoticeTime())
                .build());

        return notice.getId();
    }

    @Override
    @Transactional
    public void deleteNotice(Long noticeId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));

        boolean exists = noticeRepository.existsByIdAndMember(noticeId, member);
        if(exists){
            noticeRepository.deleteById(noticeId);
        } else {
            throw new NotFoundNoticeId(noticeId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> getNotices(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new NotFoundMemberId(memberId));

        Page<Notice> notices = noticeRepository.findByMemberAndNoticeTimeBefore(member, LocalDateTime.now(), pageable);
        return notices.map(NoticeResponseDto::of);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNewNoticesCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberId(memberId));
        return noticeRepository.countByMemberAndNoticeTimeBefore(member, LocalDateTime.now());
    }
}
