package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Repository.Post.CommentRepository;
import com.baconbao.mxh.Services.Service.Post.CommentService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        try {
            if (comment.getId() == null) {
                comment.setId(getGenerationId());
            }
            commentRepository.save(comment);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.COMMENT_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    @Override
    public Comment findById(Long id) {
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            return comment.isPresent() ? comment.get() : null;
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

}
