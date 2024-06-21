package com.baconbao.mxh.Config.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.baconbao.mxh.Models.Message.Message;

@Component
public class SocketWeb {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    public void sendMessage(Message message) {
        simpMessagingTemplate.convertAndSendToUser(message.getUserTo().getEmail(),"/queue/messages", message);
    }
}
