package com.CStudy.domain.notice.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeSearchDto {

    private LocalDateTime lastViewTime;
}
