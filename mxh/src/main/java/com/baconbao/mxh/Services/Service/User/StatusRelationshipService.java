package com.baconbao.mxh.Services.Service.User;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User.StatusRelationship;

@Service
public interface StatusRelationshipService {
    StatusRelationship findById(Long id);
}
