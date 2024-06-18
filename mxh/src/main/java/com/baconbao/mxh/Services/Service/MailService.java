package com.baconbao.mxh.Services.Service;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Mail;
@Service
public interface MailService {
    void sendMail(Mail mail);
    Mail getMail(String mailTo,String content,String subject);
}
