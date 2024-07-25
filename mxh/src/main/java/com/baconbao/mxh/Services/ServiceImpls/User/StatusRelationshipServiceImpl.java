package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.User.StatusRelationship;
import com.baconbao.mxh.Repository.User.StatusRelationshipRepository;
import com.baconbao.mxh.Services.Service.User.StatusRelationshipService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatusRelationshipServiceImpl implements StatusRelationshipService {
    @Autowired
    private StatusRelationshipRepository statusRelationshipRepository;

    @Override
    public StatusRelationship findById(Long id) {
        log.debug("Fetching status relationship with id: {}", id);
        Optional<StatusRelationship> statusRelationship = statusRelationshipRepository.findById(id);
        if (statusRelationship.isPresent()) {
            return statusRelationship.get();
        }
        throw new CustomException(ErrorCode.STATUS_NOT_FOUND);
    }

}
