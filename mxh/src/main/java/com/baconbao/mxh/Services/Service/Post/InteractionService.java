package com.baconbao.mxh.Services.Service.Post;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Post.Interaction;

@Service
public interface InteractionService {
    Interaction findById(Long id);
    void saveInteraction(Interaction interaction);
    
}
