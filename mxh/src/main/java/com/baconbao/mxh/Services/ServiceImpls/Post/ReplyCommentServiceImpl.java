package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.baconbao.mxh.Models.Post.ReplyComment;
import com.baconbao.mxh.Repository.Post.ReplyCommentRepository;
import com.baconbao.mxh.Services.Service.Post.ReplyCommentService;

public class ReplyCommentServiceImpl implements ReplyCommentService{
    @Autowired
    private ReplyCommentRepository replyCommentRepository;
    @Override
    public void save(ReplyComment comment) {
     replyCommentRepository.save(comment);
    }

    @Override
    public Long getGenerationId() {
           UUID uuid = UUID.randomUUID();
            return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }
    
}
