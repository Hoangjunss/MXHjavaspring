package com.baconbao.mxh.Services.ServiceImpls;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Image;
import com.baconbao.mxh.Services.Service.ImageService;
import com.baconbao.mxh.Repository.ImageRepository;

@Service
public class ImageServiceImpls implements ImageService {
   @Autowired
   private ImageRepository imageRepository;


   
   @Override
   public void saveImage(Image image) {
      Image img = new Image();
      img.setId(getGenerationId());
      img.setUrlImage(image.getUrlImage());
      imageRepository.save(img);
   }

   @Override
   public Long getGenerationId() {
      UUID uuid = UUID.randomUUID();
      return uuid.getMostSignificantBits() & Long.MAX_VALUE;
   }

   @Override
   public Image findById(long id) {
      Optional<Image> img = imageRepository.findById(id);
      if (img.isPresent()) {
         return img.get();
      }
      return null;
   }

   @Override
   public Image findByImage(String url) {
      return imageRepository.findByUrlImage(url);
   }

}
