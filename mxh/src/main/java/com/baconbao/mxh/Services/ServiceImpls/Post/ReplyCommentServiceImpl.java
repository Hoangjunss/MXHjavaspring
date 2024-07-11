package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.ReplyComment;
import com.baconbao.mxh.Repository.Post.ReplyCommentRepository;
import com.baconbao.mxh.Services.Service.Post.ReplyCommentService;

@Service
public class ReplyCommentServiceImpl implements ReplyCommentService{
    @Autowired
    private ReplyCommentRepository replyCommentRepository;
    @Override
    public void save(ReplyComment comment) {
        try {
            replyCommentRepository.save(comment);
        }catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL;
    }
    
}
