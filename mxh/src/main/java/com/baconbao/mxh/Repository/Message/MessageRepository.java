package com.baconbao.mxh.Repository.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baconbao.mxh.DTO.RelationshipDTO;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;

import jakarta.transaction.Transactional;

public interface MessageRepository extends JpaRepository<Message, Long> {
        //Tìm tin nhắn của 2 user từ gần nhất đến xa nhất
        @Query("SELECT m FROM Message m " +
                        "WHERE (m.userFrom = :firstUser AND m.userTo = :secondUser) " +
                        "OR (m.userFrom = :secondUser AND m.userTo = :firstUser) " +
                        "ORDER BY m.createAt ASC")
        List<Message> findAllMessagesBetweenTwoUsers(@Param("firstUser") User firstUser,
                        @Param("secondUser") User secondUser); 


        @Query("SELECT m FROM Message m WHERE m.content LIKE %?1 ")
        List<Message> findByContentLike(String content);

        //Đếm số tin nhắn chưa xem của  user
        @Query("SELECT count(m.id) FROM Message m " +
                        "WHERE ((m.userFrom = :firstUser AND m.userTo = :secondUser) " +
                        "AND m.isSeen=false )")
        int CountMessageBetweenTwoUserIsSeen(@Param("firstUser") User firstUser,
                        @Param("secondUser") User secondUser);

        //Cập nhật trạng thái tin nhắn
        @Modifying
        @Transactional
        @Query("UPDATE Message m SET m.isSeen = true WHERE m.relationship = :relationship AND m.userTo = :userTo")
        void seenMessage(@Param("relationship") Relationship relationship,
                        @Param("userTo") User userTo);

        @Query("SELECT m.userFrom.id,m.content, MAX(m.createAt) " +
                        "FROM Message m " +
                        "WHERE m.userTo = :userTo AND m.isSeen = false " +
                        "GROUP BY m.userFrom.id, m.content " +
                        "ORDER BY MAX(m.createAt) ASC")
                 List<Object[]> countUnseenMessageByUserTo(@Param("userTo") User userTo);
}