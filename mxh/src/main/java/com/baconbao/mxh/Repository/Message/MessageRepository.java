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
        //sửa lại
        @Query("SELECT m.userFrom.id, count(m.id) FROM Message m " +
                        "WHERE  m.userTo = :secondUser " +
                        "AND m.isSeen=false  GROUP BY m.userFrom.id")
        List<Object[]> countMessageBetweenTwoUserIsSeen(
                        @Param("secondUser") User secondUser);

        //Cập nhật trạng thái tin nhắn
        @Modifying
        @Transactional
        @Query("UPDATE Message m SET m.isSeen = true WHERE m.relationship = :relationship AND m.userTo = :userTo")
        void seenMessage(@Param("relationship") Relationship relationship,
                        @Param("userTo") User userTo);

        @Query(value = "SELECT " +
        "CASE " +
        "    WHEN m.from_user_id = :userId THEN m.to_user_id " +
        "    ELSE m.from_user_id " +
        "END AS oppositeUserId, " +
        "MAX(m.create_at) AS latestMessageTime, " +
        "(SELECT m2.content " +
        " FROM message AS m2 " +
        " WHERE (m2.from_user_id = m.from_user_id AND m2.to_user_id = m.to_user_id) " +
        "    OR (m2.from_user_id = m.to_user_id AND m2.to_user_id = m.from_user_id) " +
        " ORDER BY m2.create_at DESC LIMIT 1) AS latestMessageContent " +
        "FROM message AS m " +
        "WHERE m.to_user_id = :userId OR m.from_user_id = :userId " +
        "GROUP BY CASE " +
        "    WHEN m.from_user_id = :userId THEN m.to_user_id " +
        "    ELSE m.from_user_id " +
        "END " +
        "ORDER BY latestMessageTime ASC", nativeQuery = true)
        List<Object[]> countUnseenMessageByUserTo(@Param("userId") Long userId);
}