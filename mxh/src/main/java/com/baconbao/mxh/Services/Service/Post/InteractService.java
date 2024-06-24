package com.baconbao.mxh.Services.Service.Post;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Post.Interact;

@Service
public interface InteractService {
    Interact findById(Long id);
}
