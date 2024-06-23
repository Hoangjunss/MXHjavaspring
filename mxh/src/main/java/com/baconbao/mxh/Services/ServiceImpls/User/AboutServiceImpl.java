package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.AboutDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.User.About;
import com.baconbao.mxh.Repository.User.AboutRepository;
import com.baconbao.mxh.Services.Service.User.AboutService;

@Service
public class AboutServiceImpl implements AboutService {
    @Autowired
    public AboutRepository aboutRepository;

    @Override
    public About getAbout(AboutDTO aboutDTO) {
        About about = new About();
        about.setId(aboutDTO.getId());
        about.setName(aboutDTO.getName());
        return about;
    }

    @Override
    public AboutDTO getAboutDTO(About about) {
        AboutDTO aboutDTO = new AboutDTO();
        aboutDTO.setId(about.getId());
        aboutDTO.setName(about.getName());
        return aboutDTO;

    }

    @Override
    public void save(About about) {
        aboutRepository.save(about);
    }

    @Override
    public List<About> fillAll() {
        try {
            return aboutRepository.findAll();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public About findById(long aboutId) {
        Optional<About> about = aboutRepository.findById(aboutId);
        if(about.isPresent()){
            return about.get();
        }
        throw new CustomException(ErrorCode.ABOUT_NOT_FOUND);
    }

}
