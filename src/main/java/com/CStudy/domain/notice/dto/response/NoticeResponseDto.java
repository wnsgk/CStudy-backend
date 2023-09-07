package com.CStudy.domain.notice.dto.response;

import com.CStudy.domain.notice.dto.request.NoticeRequest;
import com.CStudy.domain.notice.entitiy.Notice;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class NoticeResponseDto {
    private Long questionId;
    private LocalDateTime noticeTime;

    public static NoticeResponseDto of(Notice notice){
        return NoticeResponseDto.builder()
                .questionId(notice.getQuestion().getId())
                .noticeTime(notice.getNoticeTime())
                .build();
    }
}
