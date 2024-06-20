package com.baconbao.mxh.Services.Service.Post;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.PostDTO;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.Post.Status;

@Service
public interface PostService {
    List<Post> findByStatus(Status status);
    Post findById(long id);
    void save(Post post);
    void delete(long id);
    Long getGenerationId();
    Post getPost(PostDTO postDTO);
    PostDTO getPostDTO(Post post);
}
