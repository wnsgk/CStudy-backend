package com.CStudy.domain.notice.controller;

import com.CStudy.domain.notice.application.NoticeService;
import com.CStudy.domain.notice.dto.request.NoticeRequest;
import com.CStudy.domain.notice.dto.response.NewNoticeResponse;
import com.CStudy.domain.notice.dto.response.NoticeResponseDto;
import com.CStudy.global.util.IfLogin;
import com.CStudy.global.util.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotice(
            @IfLogin LoginUserDto loginUserDto,
            @RequestBody NoticeRequest request
    ) {
        noticeService.saveNotice(request, loginUserDto.getMemberId());
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public NewNoticeResponse getNewNotice (
            @IfLogin LoginUserDto loginUserDto
    ) {
        Integer count = noticeService.getNewNoticesCount(loginUserDto.getMemberId());

        return NewNoticeResponse.builder()
                .num(count)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<NoticeResponseDto> findNoticeWithPage(
            @IfLogin LoginUserDto loginUserDto,
            @PageableDefault(sort = {"noticeTime"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return noticeService.getNotices(loginUserDto.getMemberId(), pageable);
    }


    @DeleteMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotice(@PathVariable Long noticeId, @IfLogin LoginUserDto loginUserDto) {
        noticeService.deleteNotice(noticeId, loginUserDto.getMemberId());
    }

}
