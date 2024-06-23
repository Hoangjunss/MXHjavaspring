package com.baconbao.mxh.Services.Service.User;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.AboutDTO;
import com.baconbao.mxh.Models.User.About;

@Service
public interface AboutService {
    List<About> fillAll();
    About findById(long aboutId);
    About getAbout(AboutDTO aboutDTO);
    AboutDTO getAboutDTO(About about);
    void save(About about);

}