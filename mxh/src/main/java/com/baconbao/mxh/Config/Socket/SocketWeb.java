package com.baconbao.mxh.Config.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.baconbao.mxh.DTO.MessageDTO;
import com.baconbao.mxh.DTO.UserMessageDTO;
import com.baconbao.mxh.Models.Message.Message;

@Component
public class SocketWeb {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    public void sendMessage(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        UserMessageDTO userMessageDTO = new UserMessageDTO(message.getUserFrom());
        messageDTO.setId(message.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setCreateAt(message.getCreateAt());
        messageDTO.setUserFrom(userMessageDTO);
        simpMessagingTemplate.convertAndSendToUser(message.getUserTo().getEmail(),"/queue/messages", messageDTO);
    }
}
