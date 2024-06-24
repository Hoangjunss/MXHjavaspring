package com.baconbao.mxh.Services.Service.Post;

<<<<<<< HEAD

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Post.Comment;
=======

import com.baconbao.mxh.Models.Post.Comment;

>>>>>>> b89e2c3d88d06b3be007534011ffb7d34cc1a26e

@Service
public interface CommentService {
    void save(Comment comment);
    Long getGenerationId();
    Comment findById(Long id);

}
