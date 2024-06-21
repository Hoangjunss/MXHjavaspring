package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public void addUser(Relationship relationship) {
        if(relationship.getId()==null){
            relationship.setId(getGenerationId());
        }
        relationshipRepository.save(relationship);
    }

    @Override
    public List<Relationship> findAllByUserOne(User user1) {
        return relationshipRepository.findAllByUserOneId(user1);
    }
    //hello

    @Override
    public Relationship findById(Long id) {
        Optional<Relationship> relationship = relationshipRepository.findById(id);
        if(relationship.isPresent()){
            return relationship.get();
        }else{
            return null;
        }
    }

    @Override
    public Relationship findRelationship(User userOne, User userTwo) {
        return relationshipRepository.findRelationship(userOne, userTwo);
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }

}
