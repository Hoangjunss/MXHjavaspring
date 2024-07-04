package com.baconbao.mxh.Services.ServiceImpls.User;

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

@Service
public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;
    @Autowired
    private StatusRelationshipService statusService;

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
    public int countfriend(User user, StatusRelationship status) {
        return relationshipRepository.findAllByUserOneId(user, status).size();
    }

}
