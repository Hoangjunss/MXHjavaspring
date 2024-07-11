package com.baconbao.mxh.Services.ServiceImpls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Mail;
import com.baconbao.mxh.Models.VerifycationToken;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.VerifycationRepository;
import com.baconbao.mxh.Services.Service.MailService;
import com.baconbao.mxh.Services.Service.VerifycationTokenService;
import com.baconbao.mxh.Services.Service.User.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

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
        try {
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
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.NOTIFICATION_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    //xac nhan token tai khoan
    @Override
    public void confirmUser(Long token) {
        try {
            VerifycationToken verifycationToken = findById(token);
            User user = new User();
            user.setFirstName(verifycationToken.getFirstName());
            user.setLastName(verifycationToken.getLastName());
            user.setEmail(verifycationToken.getEmail());
            user.setPassword(verifycationToken.getPassword());
            //neu xac nhan duoc thi luu user va xoa token
            userService.saveUser(user);
            verifycationTokenRepository.delete(verifycationToken);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.NOTIFICATION_UNABLE_TO_UPDATE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    //tao id token radom
    private long generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL; // Lấy phần most significant bits của UUID và đảm bảo không âm (most significant bit là bit đầu tiên của UUID)
    }

    //tim kiem token theo id
    @Override
    public VerifycationToken findById(Long id) {
        try {
            Optional<VerifycationToken> token = verifycationTokenRepository.findById(id);
            return token.isPresent() ? token.get() : null;
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
    }

    @Scheduled(fixedDelay = 60000) // Chạy mỗi 1 phút
    @Transactional
    @Override
    public void cleanupExpiredTokens() { // Xóa các token đã hết hạn
        try {
            LocalDateTime expiryTime = LocalDateTime.now(); // Xóa các token đã tạo từ 5 phút trước
            List<VerifycationToken> tokens = verifycationTokenRepository.findExpiredVerificationTokens(expiryTime); //tim token da tao tu 5p truoc
            for (VerifycationToken token : tokens) { //duyet qua cac token
                verifycationTokenRepository.delete(token);//xoa token
            }
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.NOTIFICATION_UNABLE_TO_UPDATE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

}
