package com.baconbao.mxh.Services.Service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.ImageDTO;
import com.baconbao.mxh.Models.Image;

@Service
public interface ImageService {
    Image findById(long id);
    void saveImage(Image image);
    Long getGenerationId();
    Image findByImage(String url);
}
