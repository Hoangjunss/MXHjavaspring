package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Repository.Post.ImageRepository;
import com.baconbao.mxh.Services.Service.Post.ImageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageServiceImpls implements ImageService {
   @Autowired
   private ImageRepository imageRepository;

   @Override
   public void saveImage(Image image) {
      try {
         log.info("Save image");
         Image img = Image.builder()
               .id(getGenerationId())
               .urlImage(image.getUrlImage())
               .build();
         imageRepository.save(img);
      } catch (DataIntegrityViolationException e) {
         log.error("Unable to save");
         throw new CustomException(ErrorCode.IMAGE_UNABLE_TO_SAVE);
      } catch (DataAccessException e) {
         throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
      }
   }

   @Override
   public Long getGenerationId() {
      UUID uuid = UUID.randomUUID();
      return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
   }

   @Override
   public Image findById(long id) {
      log.info("Find image by id: {}", id);
      Optional<Image> img = imageRepository.findById(id);
      if (img.isPresent()) {
         return img.get();
      }
      log.error("Image not found by id: {}", id);
      throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
   }

   @Override
   public Image findByImage(String url) {
      log.info("Finding image by URL: {}", url);
      Image img = imageRepository.findByUrlImage(url);
      if (img == null) {
         log.error("Image not found by URL: {}", url);
         throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
      }
      return img;
   }

}
