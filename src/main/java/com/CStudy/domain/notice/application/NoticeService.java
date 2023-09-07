package com.CStudy.domain.notice.application;

import com.CStudy.domain.notice.dto.request.NoticeRequest;
import com.CStudy.domain.notice.dto.response.NoticeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    Long saveNotice(NoticeRequest request, Long memberId);

    void deleteNotice(Long noticeId, Long memberId);

    Page<NoticeResponseDto> getNotices(Long memberId, Pageable pageable);

    Integer getNewNoticesCount(Long memberId);
}
