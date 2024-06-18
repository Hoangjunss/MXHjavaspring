package com.baconbao.mxh.Services.ServiceImpls;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.baconbao.mxh.Models.Mail;
import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Models.VerifycationToken;

import com.baconbao.mxh.Services.Service.MailService;
import com.baconbao.mxh.Services.Service.UserService;
import com.baconbao.mxh.Services.Service.VerifycationTokenService;

import lombok.AllArgsConstructor;
import lombok.val;
@AllArgsConstructor
public class VerifycationTokenServiceImpls implements VerifycationTokenService {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    

    @Override
    public void registerUser(User user) {
       Long id=generateToken();
       VerifycationToken verifycationToken=new VerifycationToken();
       verifycationToken.setId(id);
       verifycationToken.setEmail(user.getEmail());
       verifycationToken.setFirstName(user.getFirstName());
       verifycationToken.setLastName(user.getLastName());
       verifycationToken.setPassword(user.getPassword());
       LocalDateTime localDateTime=LocalDateTime.now().plusMinutes(5);
       Date date= Date.valueOf(localDateTime.toLocalDate());
       verifycationToken.setSetExpiryDate(date);
       Mail mail= mailService.getMail( user.getEmail(), "http://localhost:8080/confirmUser?token="+verifycationToken.getId(), "Xác nhận tài khoản");
       mailService.sendMail(mail);
    }

    @Override
    public void confirmUser(Long token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'confirmUser'");
    }
      private long generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;  // Lấy phần most significant bits của UUID và đảm bảo không âm
    }
    
}
