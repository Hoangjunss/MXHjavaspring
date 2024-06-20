package com.baconbao.mxh.Services.Service.User;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User.User;

@Service
public interface RelationshipService {
    void addUser(User userOne, User userTwo);
}
