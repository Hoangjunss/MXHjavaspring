package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.RelationshipRepository;
import com.baconbao.mxh.Services.Service.User.RelationshipService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;

    @Override
    public void addUser(Relationship relationship) {
        try {
            if (relationship.getId() == null) {
                relationship.setId(getGenerationId());
            }
            relationshipRepository.save(relationship);
        } catch (DataIntegrityViolationException e) { //DataIntegrityViolationException xảy ra khi cố gắng thực hiện một thao tác làm suy yếu tính toàn vẹn dữ liệu.
            throw new CustomException(ErrorCode.RELATIONSHIP_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public List<Relationship> findAllByUserOne(User user1) {
        try {
            return relationshipRepository.findAllByUserOneId(user1);
        } catch (EntityNotFoundException e) { //EntityNotFoundException xảy ra khi không tìm thấy một thực thể (entity) cụ thể trong cơ sở dữ liệu.

            throw new CustomException(ErrorCode.RELATIONSHIP_NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Relationship findById(Long id) {
        Optional<Relationship> relationship = relationshipRepository.findById(id);
        if (relationship.isPresent()) {
            return relationship.get();
        } else {
            throw new CustomException(ErrorCode.RELATIONSHIP_NOT_FOUND);
        }
    }

    @Override
    public Relationship findRelationship(User userOne, User userTwo) {
        Relationship relationship = relationshipRepository.findRelationship(userOne, userTwo);
        if (relationship != null) {
            return relationship;
        } else {
            throw new CustomException(ErrorCode.RELATIONSHIP_NOT_FOUND);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }

}
