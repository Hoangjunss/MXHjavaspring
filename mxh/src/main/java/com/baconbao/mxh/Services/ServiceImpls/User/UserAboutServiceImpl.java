package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAboutServiceImpl implements UserAboutService {
    @Autowired
    private UserAboutRepository userAboutRepository;

    @Override
    public UserAbout findById(Long id) {
        log.info("Finding user about by id: {}", id);
        Optional<UserAbout> userAbout = userAboutRepository.findById(id);
        if (userAbout.isPresent()) {
            return userAbout.get();
        }
        log.error("User not found with id: {}", id);
        throw new CustomException(ErrorCode.USER_ABOUT_NOT_FOUND);
    }

    @Override
    public List<UserAbout> findByUser(User user) {
        log.info("Finding user abouts by user: {}", user.getEmail());
        List<UserAbout> userAbout = userAboutRepository.findByUser(user);
        if (userAbout != null) {
            return userAbout;
        }
        log.error("User not found with id: {}", user.getEmail());
        throw new CustomException(ErrorCode.USER_ABOUT_NOT_FOUND);
    }

    @Override
    public void save(UserAbout userAbout) {
        try {
            log.info("Saving user about: {}", userAbout);
            UserAbout userAbout2 = findByUserAndAbout(userAbout.getUser(), userAbout.getAbout());
            if (userAbout2 == null) {
                userAbout.setId(getGenerationId());
            } else {
                userAbout.setId(userAbout2.getId());
            }
            userAboutRepository.save(userAbout);
        } catch (DataIntegrityViolationException e) {
            log.error("Saved flied", userAbout.getId());
            throw new CustomException(ErrorCode.USER_ABOUT_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    @Override
    public UserAbout findByUserAndAbout(User user, About about) {
        log.info("Finding user about by user: {} and about: {}", user.getEmail(), about.getId());
        UserAbout userAbout = userAboutRepository.findByUserAndAbout(user, about);
        try {
            return userAbout;
        } catch (EntityNotFoundException e) {
            log.error("User not found with email: {}", user.getEmail());
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_FOUND);
        } catch (NonUniqueResultException e) {
            throw new CustomException(ErrorCode.NON_UNIQUE_RESULT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

}
