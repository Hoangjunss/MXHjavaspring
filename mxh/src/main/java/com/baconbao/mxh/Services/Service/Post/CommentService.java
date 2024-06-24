package com.baconbao.mxh.Services.Service.Post;


import com.baconbao.mxh.Models.Post.Comment;


public interface CommentService {
    void save(Comment comment);
    Long getGenerationId();
    Comment findById(Long id);

}
