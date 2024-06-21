package com.baconbao.mxh.Repository.Message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.User.Message;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByUserToIdAndUserFromId(Long userToId,Long userFromId);
}
