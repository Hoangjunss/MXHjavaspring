package com.baconbao.mxh.Services.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Post;

public interface PostService {
    List<Post> findAll();
    Post findById(long id);
    void save(Post post);
    void delete(long id);
}
