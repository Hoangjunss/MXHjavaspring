package com.baconbao.mxh.Services.ServiceImpls;

import org.springframework.beans.factory.annotation.Autowired;

import com.baconbao.mxh.Models.Image;
import com.baconbao.mxh.Services.Service.ImageService;
import com.baconbao.mxh.Repository.ImageRepository;


public class ImageServiceImpls implements ImageService {
   @Autowired
   private ImageRepository imageRepository;
    @Override
    public void saveImage(Image image) {
       imageRepository.save(image);
    }

    
}
