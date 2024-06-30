package com.baconbao.mxh.Repository.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;

import jakarta.transaction.Transactional;

public interface MessageRepository extends JpaRepository<Message, Long> {

        @Query("SELECT m FROM Message m " +
                        "WHERE (m.userFrom = :firstUser AND m.userTo = :secondUser) " +
                        "OR (m.userFrom = :secondUser AND m.userTo = :firstUser) " +
                        "ORDER BY m.createAt ASC")
        List<Message> findAllMessagesBetweenTwoUsers(@Param("firstUser") User firstUser,
                        @Param("secondUser") User secondUser); 

                @Query("SELECT m FROM Message m WHERE m.content LIKE %?1 ")
        List<Message> findByContentLike(String content);

        @Query("SELECT count(m.id) FROM Message m " +
                        "WHERE ((m.userFrom = :firstUser AND m.userTo = :secondUser) " +
                        "OR (m.userFrom = :secondUser AND m.userTo = :firstUser))"+
                        "AND m.isSeen=false ")
        int CountMessageBetweenTwoUserIsSeen(@Param("firstUser") User firstUser,
                        @Param("secondUser") User secondUser);

        @Modifying
        @Transactional
        @Query("UPDATE Message m SET m.isSeen = true WHERE m.relationship = :relationship AND m.userTo = :userTo")
        void seenMessage(@Param("relationship") Relationship relationship,
                        @Param("userTo") User userTo);

}