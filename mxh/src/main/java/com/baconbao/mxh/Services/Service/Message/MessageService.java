package com.baconbao.mxh.Services.Service.Message;



import com.baconbao.mxh.Models.User.User;

import java.util.List;

import com.baconbao.mxh.Models.User.Message;


public interface MessageService {
    List<Message> messageFromUser(User userFrom,User userTo);
    void sendMessage(Message message);
}
