package com.baconbao.mxh.Services.Service.Post;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.PostDTO;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.Post.Status;

@Service
public interface PostService {
    List<Post> findByActiveAndStatus(boolean active, Status status); 
    Post findById(Long id);
    void save(Post post);
    void delete(Long id);
    Long getGenerationId();
    Post getPost(PostDTO postDTO);
    PostDTO getPostDTO(Post post);
}
