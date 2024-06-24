package com.baconbao.mxh.Services.Service.Post;


import com.baconbao.mxh.Models.Post.ReplyComment;

public interface ReplyCommentService {
    void save(ReplyComment comment);
    Long getGenerationId();
}
