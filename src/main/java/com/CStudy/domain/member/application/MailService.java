package com.CStudy.domain.member.application;

import javax.mail.MessagingException;

public interface MailService {

    public String sendEmail(String recipientEmail) throws MessagingException;

    public Boolean verifyCode(String email, String code);
}
