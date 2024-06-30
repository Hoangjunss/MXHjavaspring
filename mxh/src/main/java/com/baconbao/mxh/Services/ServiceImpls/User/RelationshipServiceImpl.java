package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.persistence.EntityNotFoundException;

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
<<<<<<< HEAD
    public List<Relationship> findAllByUserOne(User user1) {
      
=======
    public List<Relationship> findAllByUserOne(User user1) { // Truy vấn và trả về tất cả mối quan hệ mà user1 tham gia
        try {
>>>>>>> 7f4ad6663775304a7a7f11d2581c5b13a1c382ef
            return relationshipRepository.findAllByUserOneId(user1);
       
    }

    @Override
    public Relationship findById(Long id) { // Truy vấn và trả về mối quan hệ với id tương ứng
        Optional<Relationship> relationship = relationshipRepository.findById(id);
        if (relationship.isPresent()) { 
            return relationship.get();
        } return null;
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
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL;
    }

    @Override
<<<<<<< HEAD
    public List<Relationship> findAllByUserOneId(User user) {
       
            StatusRelationship status = statusService.findById(1L);
=======
    public List<Relationship> findAllByUserOneId(User user) { // Truy vấn và trả về tất cả mối quan hệ mà user tham gia
        try {
            StatusRelationship status = statusService.findById(2L); 
>>>>>>> 7f4ad6663775304a7a7f11d2581c5b13a1c382ef
            System.out.println(status.getStatus()+ "RELATIONSHIP SEARCH STATUS");
            return relationshipRepository.findAllByUserOneId(user, status);
      
    }

    @Override
    public List<RelationshipDTO> orderByCreateAt(List<RelationshipDTO> relationships) {
<<<<<<< HEAD
       
            relationships.sort(Comparator.comparing(RelationshipDTO::getCreateAt).reversed());
=======
        try {
            relationships.sort(Comparator.comparing(RelationshipDTO::getCreateAt).reversed()); // comparator.comparing(RelationshipDTO::getCreateAt).reversed() tạo ra một bộ so sánh các đối tượng RelationshipDTO dựa trên thời gian tạo và sắp xếp theo thời gian tạo giảm dần
>>>>>>> 7f4ad6663775304a7a7f11d2581c5b13a1c382ef
            return relationships;
       
    }

    @Override
    public Relationship findByMessage(List<Message> messages) {
       
            return relationshipRepository.findByMessages(messages);
       
    }

}
