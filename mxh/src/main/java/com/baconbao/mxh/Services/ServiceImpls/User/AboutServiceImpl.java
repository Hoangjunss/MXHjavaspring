package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.User.About;
import com.baconbao.mxh.Models.User.UserAbout;
import com.baconbao.mxh.Repository.User.AboutRepository;
import com.baconbao.mxh.Repository.User.UserAboutRepository;
import com.baconbao.mxh.Services.Service.User.AboutService;

import lombok.extern.slf4j.Slf4j;

import com.baconbao.mxh.Models.User.User;

@Service
@Slf4j
public class AboutServiceImpl implements AboutService {
    @Autowired
    public AboutRepository aboutRepository;
    @Autowired
    private UserAboutRepository userAboutRepository;

    @Override
    public void save(About about) {
        log.info("Save about");
        aboutRepository.save(about);
    }

    @Override
    public List<About> fillAll() {
        try {
            log.info("Find all about");
            return aboutRepository.findAll();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public About findById(long aboutId) {
        log.info("Find about by id: {}", aboutId);
        Optional<About> about = aboutRepository.findById(aboutId);
        if (about.isPresent()) {
            return about.get();
        }
        log.error("About is not found with id: {}", aboutId);
        throw new CustomException(ErrorCode.ABOUT_NOT_FOUND);
    }

    public List<UserAbout> findByUser(User user) {
        try {
            log.info("Find all about by user: {}", user);
            return userAboutRepository.findByUser(user);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
