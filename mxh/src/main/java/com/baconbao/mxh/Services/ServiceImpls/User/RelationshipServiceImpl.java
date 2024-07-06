package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.RelationshipDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.StatusRelationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.RelationshipRepository;
import com.baconbao.mxh.Services.Service.User.RelationshipService;
import com.baconbao.mxh.Services.Service.User.StatusRelationshipService;
import com.baconbao.mxh.Services.Service.User.UserService;
@Service

public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;
    @Autowired
    private StatusRelationshipService statusService;
    @Autowired
    private UserService userService;

    @Override
    public void addUser(Relationship relationship) {

        if (relationship.getId() == null) {
            relationship.setId(getGenerationId());
        }
        relationshipRepository.save(relationship);
    }

    @Override

    public List<Relationship> findAllByUserOne(User user1) {
        return relationshipRepository.findAllByUserOneId(user1);

    }

    @Override
    public Relationship findById(Long id) { // Truy vấn và trả về mối quan hệ với id tương ứng
        Optional<Relationship> relationship = relationshipRepository.findById(id);
        if (relationship.isPresent()) {
            return relationship.get();
        }
        return null;
    }

    @Override
    public Relationship findRelationship(User userOne, User userTwo) { // Truy vấn và trả về mối quan hệ giữa hai user
        Relationship relationship = relationshipRepository.findRelationship(userOne, userTwo);
        if (relationship != null) {
            return relationship;
        }
        return null;
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    @Override

    public List<Relationship> findAllByUserOneId(User user) {

        StatusRelationship status = statusService.findById(2L); // why sao set cứng ???????????

        System.out.println(status.getStatus() + "RELATIONSHIP SEARCH STATUS");
        return relationshipRepository.findAllByUserOneId(user, status);

    }

    @Override
    public List<RelationshipDTO> orderByCreateAt(List<RelationshipDTO> relationships) {
        try {
            relationships.sort(Comparator.comparing(RelationshipDTO::getCreateAt).reversed());
            return relationships;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Relationship findByMessage(List<Message> messages) {
        return relationshipRepository.findByMessages(messages);
    }

    @Override
    public int countMutualFriends(User user, Long friendId) {
        // tìm bạn bè của user
        List<User> relationships = relationshipRepository.findFriendsByUserAndStatus(user, statusService.findById(2L));
        
        // tìm bạn bè của bạn bè
        User friend = userService.findById(friendId);
        List<User> friendFriends = relationshipRepository.findFriendsByUserAndStatus(friend, statusService.findById(2L));

        // tìm số lượng bạn chung
        int count = 0;
        for (User u : relationships) {
            if (friendFriends.contains(u)) {
                count++;
            }
        }
        return count;
    }

    public List<User> findFriends(User user) {
        List<Relationship> relationships = relationshipRepository.findFriendByUser(user);
        List<User> friends = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.getUserOne().equals(user)) {
                friends.add(relationship.getUserTwo());
            } else {
                friends.add(relationship.getUserOne());
            }
        }
        return friends;
    }

    public List<User> findNotFriends(User user) {
        List<Relationship> relationships = relationshipRepository.findNotFriendsByUser(user);
        List<User> notFriends = new ArrayList<>();
        for (Relationship relationship : relationships) {
            if (relationship.getUserOne().equals(user)) {
                notFriends.add(relationship.getUserTwo());
            } else {
                notFriends.add(relationship.getUserOne());
            }
        }
        return notFriends;
    }
}

   
