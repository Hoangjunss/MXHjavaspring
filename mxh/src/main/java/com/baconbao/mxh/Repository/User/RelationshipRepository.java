package com.baconbao.mxh.Repository.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.StatusRelationship;
import com.baconbao.mxh.Models.User.User;
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

        @Query("SELECT r FROM Relationship r WHERE (r.userOne = :firstUser OR r.userTwo = :firstUser) AND r.status = :status")
        List<Relationship> findAllByUserOneId(@Param("firstUser") User firstUser, @Param("status") StatusRelationship status);

        Relationship findByMessages(List<Message> messages);



}
