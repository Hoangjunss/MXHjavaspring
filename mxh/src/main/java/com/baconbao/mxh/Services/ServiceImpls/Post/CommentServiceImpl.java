package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Repository.Post.CommentRepository;
import com.baconbao.mxh.Services.Service.Post.CommentService;

public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

   
        @Override
        public Long getGenerationId() {
            UUID uuid = UUID.randomUUID();
            return uuid.getMostSignificantBits() & Long.MAX_VALUE;
        }
    
    
}
