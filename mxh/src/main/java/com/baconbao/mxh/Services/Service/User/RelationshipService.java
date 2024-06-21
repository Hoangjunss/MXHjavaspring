package com.baconbao.mxh.Services.Service.User;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;

@Service
public interface RelationshipService {
<<<<<<< Updated upstream
    List<Relationship> findAllByUserOne(User user1);
    void addUser(Relationship relationship);
    Relationship findById(Long id);
    Relationship findRelationship(User userOne, User userTwo);
}
=======
    List<Relationship> findAllByUserOne(Long user1);
    void addUser(User userOne, User userTwo);
    void save(Relationship relationship);
    Relationship findById(Long id);
    Relationship relationshipFromUser(User userOne, User userTwo);
}
>>>>>>> Stashed changes
