package com.baconbao.mxh.Services.Service.User;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.AboutDTO;
import com.baconbao.mxh.Models.User.About;
import com.baconbao.mxh.Models.User.UserAbout;
import com.baconbao.mxh.Models.User.User;

@Service
public interface AboutService {

    List<About> fillAll();
    About findById(long aboutId);
    void save(About about);
    List<UserAbout> findByUser(User user);

}