package com.baconbao.mxh.Services.Service.Message;

import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.User;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    List<Message> messageFromUser(User userFrom,User userTo);
    void sendMessage(Message message);
    List<Message> findByContent(String content);
    Message findLatestMessage(User userFrom,User userTo);

}
