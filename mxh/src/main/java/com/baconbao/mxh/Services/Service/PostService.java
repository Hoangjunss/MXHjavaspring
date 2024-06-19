package com.baconbao.mxh.Services.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.PostDTO;
import com.baconbao.mxh.Models.Post;

@Service
public interface PostService {
    List<Post> findByStatus(String status);
    Post findById(long id);
    void save(Post post);
    void delete(long id);
    Long getGenerationId();
    Post getPost(PostDTO postDTO);
    PostDTO getPostDTO(Post post);
}
