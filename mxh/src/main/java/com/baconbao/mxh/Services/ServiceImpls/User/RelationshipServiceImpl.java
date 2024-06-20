package com.baconbao.mxh.Services.ServiceImpls.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.RelationshipRepository;
import com.baconbao.mxh.Services.Service.User.RelationshipService;

@Service
public class RelationshipServiceImpl implements RelationshipService{
    @Autowired
    private RelationshipRepository relationshipRepository;

    @Override
    public void addUser(User userOne, User userTwo) {
        Relationship relationship = new Relationship();
        relationship.setUserOne(userOne);
        relationship.setUserTwo(userTwo);
        relationship.setStatus("add");
        relationshipRepository.save(relationship);
    }

}
