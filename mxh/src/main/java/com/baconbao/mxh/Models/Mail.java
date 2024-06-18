package com.baconbao.mxh.Models;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data


public class Mail 
{
	private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mailBcc;
    public Mail() {
        this.mailFrom="mxhbaconbao@gmail.com";
    }

    private String mailSubject;
    private String mailContent;
    private String contentType = "text/plain";
    private List <Object> attachments;
    
    public Date getMailSendDate() {
        return new Date();
    }
}
