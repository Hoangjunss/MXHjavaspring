package com.baconbao.mxh.Services.Service;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User;

@Service
public interface RelationshipService {
    void addUser(User userOne, User userTwo);
}
