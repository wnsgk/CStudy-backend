package com.CStudy.domain.member.controller;

import com.CStudy.domain.member.application.MailService;
import com.CStudy.domain.member.application.MemberService;
import com.CStudy.domain.member.dto.request.EmailRequest;
import com.CStudy.domain.member.dto.response.EmailVerifyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;

@Tag(name = "Mail(메일 API)", description = "메일 관련 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final MemberService memberService;

    @Operation(summary = "이메일 인증 코드", description = "이메일 인증 코드 발송")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        memberService.duplicationWithEmail(emailRequest.getEmail());
        try {
            return mailService.sendEmail(emailRequest.getEmail());
        } catch (MessagingException e) {
            return "이메일 전송 실패";
        }
    }

    @Operation(summary = "회원 가입시 이메일 인증", description = "기존사용하고 있는 이메일을 통해 인증")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/email/verification")
    @ResponseStatus(HttpStatus.OK)
    public EmailVerifyResponse verification(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        return EmailVerifyResponse.of(mailService.verifyCode(email, code));
    }
}
