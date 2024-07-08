package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.User.About;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Models.User.UserAbout;
import com.baconbao.mxh.Repository.User.UserAboutRepository;
import com.baconbao.mxh.Services.Service.User.UserAboutService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserAboutServiceImpl implements UserAboutService {
    @Autowired
    private UserAboutRepository userAboutRepository;

    @Override
    public UserAbout findById(Long id) {
        Optional<UserAbout> userAbout = userAboutRepository.findById(id);
        if(userAbout.isPresent()){
            return userAbout.get();
        }
        throw new CustomException(ErrorCode.USER_ABOUT_NOT_FOUND);
    }

    @Override
    public List<UserAbout> findByUser(User user) {
        List<UserAbout> userAbout= userAboutRepository.findByUser(user);
        if(userAbout!=null){
            return userAbout;
        }
        throw new CustomException(ErrorCode.USER_ABOUT_NOT_FOUND);
    }

    @Override
    public void save(UserAbout userAbout) {
        try {
            UserAbout userAbout2 = findByUserAndAbout(userAbout.getUser(), userAbout.getAbout());
            if (userAbout2 == null) {
                userAbout.setId(getGenerationId());
            }else{
                userAbout.setId(userAbout2.getId());
            }
            userAboutRepository.save(userAbout);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e){
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL;
    }

    @Override
    public UserAbout findByUserAndAbout(User user, About about) {
        UserAbout userAbout =  userAboutRepository.findByUserAndAbout(user, about);
        try {
            return userAbout;
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_FOUND);
        } catch(Exception e){
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    
}
