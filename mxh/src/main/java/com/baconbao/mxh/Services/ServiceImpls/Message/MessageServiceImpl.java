package com.baconbao.mxh.Services.ServiceImpls.Message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Config.Socket.SocketWeb;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.Message.MessageRepository;
import com.baconbao.mxh.Services.Service.Message.MessageService;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SocketWeb socketWeb;

    @Override
    public List<Message> messageFromUser(User userFrom, User userTo) {
        return messageRepository.findAllMessagesBetweenTwoUsers(userFrom, userTo);
    }

    @Override
    public void sendMessage(Message message) {
        messageRepository.save(message);
        socketWeb.sendMessage(message);
    }

}