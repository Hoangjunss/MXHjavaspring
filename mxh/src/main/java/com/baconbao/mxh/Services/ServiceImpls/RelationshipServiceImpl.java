package com.baconbao.mxh.Services.ServiceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.Relationship;
import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Repository.RelationshipRepository;
import com.baconbao.mxh.Services.Service.RelationshipService;

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
