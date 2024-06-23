package com.baconbao.mxh.Services.Service.User;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User.About;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Models.User.UserAbout;
@Service
public interface UserAboutService {
    UserAbout findById(Long id);
    List<UserAbout> findByUser(User user);
    void save(UserAbout userAbout);
    UserAbout findByUserAndAbout(User user, About about);
}
