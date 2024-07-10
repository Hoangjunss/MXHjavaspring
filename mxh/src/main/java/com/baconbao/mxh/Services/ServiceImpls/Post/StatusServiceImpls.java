package com.baconbao.mxh.Services.ServiceImpls.Post;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Status;
import com.baconbao.mxh.Repository.Post.StatusRepository;
import com.baconbao.mxh.Services.Service.Post.StatusService;

@Service
public class StatusServiceImpls implements StatusService{
    @Autowired
    private StatusRepository statusRepository;
    @Override
    public List<Status> findAll() {
        try {
            return statusRepository.findAll();
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    @Override
    public Status findById(long id) {
        Optional<Status> status=statusRepository.findById(id);
        if(status.isPresent()){
            return status.get();
        }
        throw new CustomException(ErrorCode.STATUS_NOT_FOUND);
    }
    
}
