package com.baconbao.mxh.Services.Service;

import com.baconbao.mxh.DTO.AboutDTO;
import com.baconbao.mxh.Models.About;

public interface AboutService_Chung {
    About getAbout(AboutDTO aboutDTO);
    AboutDTO geAboutDTO(About about);
    void save(About about);

}
