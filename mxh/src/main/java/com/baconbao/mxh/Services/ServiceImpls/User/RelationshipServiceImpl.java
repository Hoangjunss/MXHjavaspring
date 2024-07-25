package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
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

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.QueryTimeoutException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;
    @Autowired
    private StatusRelationshipService statusService;
    @Autowired
    private UserService userService;

    @Override
    public void addUser(Relationship relationship) {
        try {
            log.info("Create relationship betwwen two user");
            if (relationship.getId() == null) {
                relationship.setId(getGenerationId());
            }
            relationshipRepository.save(relationship);
        } catch (DataIntegrityViolationException e) {
            log.error("Fail to created relationship betwwen to user");
            throw new CustomException(ErrorCode.RELATIONSHIP_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public List<Relationship> findAllByUserOne(User user1) {
        try {
            return relationshipRepository.findAllByUserOneId(user1);
        } catch (QueryTimeoutException e) {
            throw new CustomException(ErrorCode.QUERY_TIMEOUT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public Relationship findById(Long id) { // Truy vấn và trả về mối quan hệ với id tương ứng
        try {
            Optional<Relationship> relationship = relationshipRepository.findById(id);
            return relationship.isPresent() ? relationship.get() : null;
        } catch (NoResultException e) {
            throw new CustomException(ErrorCode.RELATIONSHIP_NOT_FOUND);
        } catch (NonUniqueResultException e) {
            throw new CustomException(ErrorCode.NON_UNIQUE_RESULT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public Relationship findRelationship(User userOne, User userTwo) { // Truy vấn và trả về mối quan hệ giữa hai user
        try {
            Relationship relationship = relationshipRepository.findRelationship(userOne, userTwo);
            return relationship != null ? relationship : null;
        } catch (NoResultException e) {
            throw new CustomException(ErrorCode.RELATIONSHIP_NOT_FOUND);
        } catch (NonUniqueResultException e) {
            throw new CustomException(ErrorCode.NON_UNIQUE_RESULT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    @Override

    public List<Relationship> findAllByUserOneId(User user) {
        try {
            StatusRelationship status = statusService.findById(1L); // why sao set cứng ???????????
            return relationshipRepository.findAllByUserOneId(user, status);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
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
        try {
            return relationshipRepository.findByMessages(messages);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    // Đếm số lượng bạn chung giữa 2 user
    @Override
    public int countMutualFriends(User user, Long friendId) {
        try {
            User friend = userService.findById(friendId);
            StatusRelationship status = statusService.findById(2L);
            return relationshipRepository.countMutualFriends(user.getId(), friend.getId(), status.getId());
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public int countfriend(User user, StatusRelationship status) {
        try {
            return relationshipRepository.findRelationshipPending(user, status).size();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
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

    @Override
    public List<Relationship> findRelationshipPending(User user) {
        try {
            return relationshipRepository.findFriendByUser(user);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
