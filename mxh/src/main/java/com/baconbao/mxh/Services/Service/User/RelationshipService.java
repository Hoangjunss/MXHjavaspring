package com.baconbao.mxh.Services.Service.User;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;

@Service
public interface RelationshipService {
    List<Relationship> findAllByUserOne(Long user1);
    void addUser(User userOne, User userTwo);
}
