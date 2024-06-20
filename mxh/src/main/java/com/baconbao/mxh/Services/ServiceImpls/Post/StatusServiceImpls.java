package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Post.Status;
import com.baconbao.mxh.Repository.Post.StatusRepository;
import com.baconbao.mxh.Services.Service.Post.StatusService;

@Service
public class StatusServiceImpls implements StatusService{
    @Autowired
    private StatusRepository statusRepository;
    @Override
    public List<Status> findAll() {
        return statusRepository.findAll();
    }
    @Override
    public Status findById(long id) {
        return statusRepository.findById(id).get();
    }
    
}
