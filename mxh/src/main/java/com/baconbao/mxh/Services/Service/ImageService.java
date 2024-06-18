package com.baconbao.mxh.Services.Service;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.ImageDTO;
import com.baconbao.mxh.Models.Image;

@Service
public interface ImageService {
    void saveImage(Image image);
    Long getGenerationId();
}
