package com.baconbao.mxh.Services.Service.Post;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Post.Status;

@Service
public interface StatusService {
    List<Status> findAll();
    Status findById(long id);
}
