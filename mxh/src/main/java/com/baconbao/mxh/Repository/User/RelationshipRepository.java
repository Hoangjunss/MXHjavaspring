package com.baconbao.mxh.Repository.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.StatusRelationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.DTO.RelationshipDTO;
import com.baconbao.mxh.Models.Message.Message;


public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
        //Xac nhan moi quan he giua 2 user
        @Query("SELECT r FROM Relationship r " +
                        "WHERE r.userOne = :firstUser OR r.userTwo = :firstUser")
        List<Relationship> findAllByUserOneId(@Param("firstUser") User firstUser);


        @Query("SELECT r FROM Relationship r " +
                        "WHERE (r.userOne = :firstUser AND r.userTwo = :secondUser) " +
                        "OR (r.userOne = :secondUser AND r.userTwo = :firstUser) ")
        Relationship findRelationship(@Param("firstUser") User firstUser,
                        @Param("secondUser") User secondUser);

        @Query("SELECT r FROM Relationship r LEFT JOIN r.messages m WHERE (r.userOne = :firstUser OR r.userTwo = :firstUser) AND r.status = :status ORDER BY m.createAt DESC")
        List<Relationship> findAllByUserOneId(@Param("firstUser") User firstUser, @Param("status") StatusRelationship status);

        Relationship findByMessages(List<Message> messages);

        // tìm bạn bè của user
        @Query("SELECT r FROM Relationship r WHERE (r.userOne = :user OR r.userTwo = :user) AND r.status.id = 1")
        List<Relationship> findFriendByUser(@Param("user") User user);

        // tìm những người không phải là bạn bè của user
        @Query("SELECT r FROM Relationship r WHERE (r.userOne = :user OR r.userTwo = :user) AND r.status.id = 4")
        List<Relationship> findNotFriendsByUser(@Param("user") User user);

       /*  @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.createAt DESC")
        List<Message> findMessagesBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

        @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.createAt DESC")
        Message findLatestMessage(@Param("user1") User user1, @Param("user2") User user2); */

        @Query("SELECT new com.baconbao.mxh.DTO.RelationshipDTO(r) FROM Relationship r")
        List<RelationshipDTO> findAllRelationshipDTO();

        // tìm bạn bè của user
        @Query("SELECT r FROM Relationship r WHERE (r.userOne = :firstUser OR r.userTwo = :firstUser) AND r.status = :status")
        List<User> findFriendsByUserAndStatus(@Param("firstUser") User firstUser, @Param("status") StatusRelationship status);

        // đếm số lượng bạn chung giữa 2 user
        @Query("SELECT COUNT(r) FROM Relationship r WHERE (r.userOne.id = :userId AND r.userTwo.id IN (SELECT r2.userTwo.id FROM Relationship r2 WHERE r2.userOne.id = :friendId AND r2.status.id = :statusId)) OR (r.userTwo.id = :userId AND r.userOne.id IN (SELECT r2.userOne.id FROM Relationship r2 WHERE r2.userTwo.id = :friendId AND r2.status.id = :statusId))")
        int countMutualFriends(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("statusId") Long statusId);
        

        // tìm mối quan hệ đang chờ xác nhận
        @Query("SELECT r FROM Relationship r WHERE (r.userOne = :user OR r.userTwo = :user) AND r.status = :status")
        List<Relationship> findRelationshipPending(@Param("user") User user, @Param("status") StatusRelationship status);

}