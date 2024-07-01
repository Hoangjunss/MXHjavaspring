package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.PostDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.Post.Status;
import com.baconbao.mxh.Repository.Post.PostRepository;
import com.baconbao.mxh.Services.Service.Post.PostService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findByActiveAndStatus(boolean active, Status idStatus) {
        try {
            return postRepository.findByIsActiveAndStatusOrderByCreateAtDesc(active, idStatus);
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Post findById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        }
        throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }

    @Override
    public void save(Post post) {
        try {
            if (post.getId() == null) {
                post.setId(getGenerationId());
            }
            postRepository.save(post);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.POST_NOT_SAVED);
        } catch (Exception e){
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Post psot = findById(id);
            postRepository.delete(psot);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.POST_NOT_SAVED);
        } catch (Exception e){
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL;
    }

    @Override
    public Post getPost(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setContent(postDTO.getContent());
        post.setCreateAt(postDTO.getCreateAt());
        post.setUpdateAt(postDTO.getUpdateAt());
        post.setStatus(postDTO.getStatus());
        return post;
    }

    @Override
    public PostDTO getPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setContent(post.getContent());
        postDTO.setCreateAt(post.getCreateAt());
        postDTO.setUpdateAt(post.getUpdateAt());
        postDTO.setStatus(post.getStatus());
        return postDTO;
    }
}
