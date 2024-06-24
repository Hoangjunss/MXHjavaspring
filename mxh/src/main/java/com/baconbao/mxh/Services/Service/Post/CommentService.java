package com.baconbao.mxh.Services.Service.Post;


import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Post.Comment;

@Service
public interface CommentService {
    void save(Comment comment);
    Long getGenerationId();


}
