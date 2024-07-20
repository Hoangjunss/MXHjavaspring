package com.baconbao.mxh.Services.Service.Post;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Post.Interaction;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.User.User;

@Service
public interface InteractionService {
    Interaction findById(Long id);
    void saveInteraction(Interaction interaction);
    Interaction findByPostAndUser(Post post, User user);
}
