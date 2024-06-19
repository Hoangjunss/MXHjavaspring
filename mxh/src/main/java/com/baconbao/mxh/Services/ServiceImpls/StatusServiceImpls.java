package com.baconbao.mxh.Services.ServiceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Status;
import com.baconbao.mxh.Repository.StatusRepository;
import com.baconbao.mxh.Services.Service.StatusService;

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
