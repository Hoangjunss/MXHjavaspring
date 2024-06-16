package com.baconbao.mxh.Services.ServiceImpls;

import org.springframework.beans.factory.annotation.Autowired;

import com.baconbao.mxh.DTO.AboutDTO;
import com.baconbao.mxh.Models.About;
import com.baconbao.mxh.Repository.AboutRepository;
import com.baconbao.mxh.Services.Service.AboutService_Chung;

import com.baconbao.mxh.Services.Service.UserService_Kien;

public class AboutService_ChungImpl implements AboutService_Chung {
   @Autowired
   public AboutRepository aboutRepository;
   
    public AboutService_ChungImpl(AboutRepository aboutRepository) {
    this.aboutRepository = aboutRepository;
}

    @Override
    public About getAbout(AboutDTO aboutDTO) {
        About about= new About();
        about.setId(aboutDTO.getId());
        about.setName(aboutDTO.getName());
        about.setDescribes(aboutDTO.getDescribe());
        return about;
    }

    @Override
    public AboutDTO geAboutDTO(About about) {
       AboutDTO aboutDTO=new AboutDTO();
       aboutDTO.setId(about.getId());
       aboutDTO.setDescribe(about.getDescribes());
       aboutDTO.setName(about.getName());
       return aboutDTO;

    }

    @Override
    public void save(About about) {
         aboutRepository.save(about);
    }

}
