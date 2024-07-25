package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.Post.PostRepository;
import com.baconbao.mxh.Services.Service.Post.PostService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findByActive(boolean active) {
        try {
            log.info("Find post is active: {}", active);
            return postRepository.findByIsActiveOrderByCreateAtDesc(active);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Post findById(Long id) {
        log.info("Find post by id: {}", id);
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        }
        log.error("Post is not found with id: {}", id);
        throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }

    @Override
    public void save(Post post) {
        try {
            log.info("Save post: {}", post);
            if (post.getId() == null) {
                post.setId(getGenerationId());
            }
            postRepository.save(post);
        } catch (DataIntegrityViolationException e) {
            log.error("Fail saved post");
            throw new CustomException(ErrorCode.POST_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            log.info("Delete post by id: {}", id);
            Post psot = findById(id);
            postRepository.delete(psot);
        } catch (DataIntegrityViolationException e) {
            log.error("Unable to delete post with id: {}", id);
            throw new CustomException(ErrorCode.POST_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    @Override
    public List<Post> findByUserPosts(User user) {
        try {
            log.info("Find post by user: {}", user);
            return postRepository.findByUserOrderByCreateAtDesc(user);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public List<Comment> findByCommentsOrderByCreateAtDesc(Post post) {
        try {
            log.info("Find post comments by post: {}", post);
            return postRepository.findByCommentsOrderByCreateAtDesc(post);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public List<Object[]> findPostAndCommentAndReplyCount(Post post, boolean active) {
        try {
            log.info("Find post and comment and reply count by post: {}", post);
            return postRepository.findPostAndCommentAndReplyCount(post, active);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Long countInteraction(Post post) {
        try {
            log.info("Count interaction by post: {}", post);
            return postRepository.countInteraction(post);
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
