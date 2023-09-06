package com.CStudy.domain.member.application.impl;

import com.CStudy.domain.member.application.MailService;
import com.CStudy.global.redis.RedisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private  String EMAIL;


    //todo : 반환 타입을 Future로 설정 / 학습
    @Async
    @Override
    @Transactional
    public String sendEmail(String recipientEmail) throws MailException, MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String key = createKey();

        helper.setFrom(EMAIL);
        helper.setTo(recipientEmail);
        helper.setSubject("회원가입 코드 메일");
        helper.setText(emailHtml(key), true);

        javaMailSender.send(message);
        redisService.setValues(recipientEmail, key, Duration.ofMinutes(3));
        return key;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean verifyCode(String email, String code) {
        String verificationCode = redisService.getValues(email);
        System.out.println("code = " + code);
        System.out.println("verificationCode = " + verificationCode);
        if(verificationCode == null){
            return false;
        }
        return code.equals(verificationCode);
    }


    @NotNull
    private static String emailHtml(String key) {
        String msgg = "<div style='margin:100px;'>";
        msgg += "<h1>안녕하세요 CS;tudy입니다!!!</h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += key + "</strong><div><br/> ";
        msgg += "</div>";
        return msgg;
    }


    private String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }
}
