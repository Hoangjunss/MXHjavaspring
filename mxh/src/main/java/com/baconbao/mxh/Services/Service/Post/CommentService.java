package com.baconbao.mxh.Services.Service.Post;

import java.util.List;

import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Models.Post.Post;

public interface CommentService {
    void save(Comment comment);
    Long getGenerationId();


}
