package com.baconbao.mxh.Services.ServiceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Mail;
import com.baconbao.mxh.Services.Service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
@Service
public class MailServiceImpl implements MailService {
 @Autowired
	private JavaMailSender javaMailSender;
    @Override
    public void sendMail(Mail mail) {
         	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
			mimeMessageHelper.setTo(mail.getMailTo());
			mimeMessageHelper.setText(mail.getMailContent());
			javaMailSender.send(mimeMessageHelper.getMimeMessage());
		} 
		catch (MessagingException e) {
			e.printStackTrace();
		}
    }
	@Override
	public Mail getMail( String mailTo, String content, String subject) {
		Mail mail = new Mail();
        
      
        mail.setMailTo(mailTo);
        mail.setMailSubject(subject);
        mail.setMailContent(content);
        return mail;
	}

    
}
