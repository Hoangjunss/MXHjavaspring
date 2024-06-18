package com.baconbao.mxh.Services.ServiceImpls;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baconbao.mxh.Models.Mail;
import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Models.VerifycationToken;
import com.baconbao.mxh.Repository.VerifycationRepository;
import com.baconbao.mxh.Services.Service.MailService;
import com.baconbao.mxh.Services.Service.UserService;
import com.baconbao.mxh.Services.Service.VerifycationTokenService;

import lombok.AllArgsConstructor;
import lombok.val;

@Service
@AllArgsConstructor
public class VerifycationTokenServiceImpls implements VerifycationTokenService {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private VerifycationRepository verifycationTokenRepository;

    //Gui mail
    @Override
    public void registerUser(User user) {
        Long id = generateToken();
        VerifycationToken verifycationToken = new VerifycationToken();
        verifycationToken.setId(id);
        verifycationToken.setEmail(user.getEmail());
        verifycationToken.setFirstName(user.getFirstName());
        verifycationToken.setLastName(user.getLastName());
        verifycationToken.setPassword(user.getPassword());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(5);
        verifycationToken.setSetExpiryDate(localDateTime);
        //Tao doi tuong mail chua link xac nhan
        Mail mail = mailService.getMail(user.getEmail(),
                "http://localhost:8080/confirmUser?token=" + verifycationToken.getId(), "Xác nhận tài khoản");
        mailService.sendMail(mail);
        verifycationTokenRepository.save(verifycationToken);
    }

    //xac nhan token tai khoan
    @Override
    public void confirmUser(Long token) {
        VerifycationToken verifycationToken = findById(token);
        User user = new User();
        user.setFirstName(verifycationToken.getFirstName());
        user.setLastName(verifycationToken.getLastName());
        user.setEmail(verifycationToken.getEmail());
        user.setPassword(verifycationToken.getPassword());
        //neu xac nhan duoc thi luu user va xoa token
        userService.saveUser(user);
        verifycationTokenRepository.delete(verifycationToken);
    }

    //tao id token radom
    private long generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE; // Lấy phần most significant bits của UUID và đảm bảo
                                                               // không âm
    }

    //tim kiem token theo id
    @Override
    public VerifycationToken findById(Long id) {
        Optional<VerifycationToken> token = verifycationTokenRepository.findById(id);
        if (token.isPresent()) {
            return token.get();
        } else
            return null;
    }

    @Scheduled(fixedDelay = 60000) // Chạy mỗi 5 phút
    @Transactional 
    @Override
    public void cleanupExpiredTokens() {
        LocalDateTime expiryTime = LocalDateTime.now(); // Xóa các token đã tạo từ 5 phút trước
        List<VerifycationToken> tokens = verifycationTokenRepository.findExpiredVerificationTokens(expiryTime); //tim token da tao tu 5p truoc
        for (VerifycationToken token : tokens) {
            verifycationTokenRepository.delete(token);//xoa token
        }
    }

}
