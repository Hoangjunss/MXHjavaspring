package com.baconbao.mxh.Services.ServiceImpls;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.PostDTO;
import com.baconbao.mxh.Models.Post;
import com.baconbao.mxh.Repository.PostRepository;
import com.baconbao.mxh.Services.Service.PostService;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findByStatus(String status) {
        return postRepository.findByStatus(status);
    }

    @Override
    public Post findById(long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public void save(Post post) {
        post.setId(getGenerationId());
        postRepository.save(post);
    }

    @Override
    public void delete(long id) {
        Post psot = findById(id);
        postRepository.delete(psot);
    }

    @Override
    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
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
