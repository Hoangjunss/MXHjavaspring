package com.baconbao.mxh.Services.ServiceImpls;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
