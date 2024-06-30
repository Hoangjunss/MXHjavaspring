package com.baconbao.mxh.Config.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.baconbao.mxh.DTO.MessageDTO;
import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.DTO.UserMessageDTO;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.Service.User.RelationshipService;

@Component
public class SocketWeb {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private RelationshipService relationshipService;

    public void sendMessage(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        UserMessageDTO userMessageDTO = new UserMessageDTO(message.getUserFrom());
        Relationship relationship = relationshipService.findRelationship(message.getUserFrom(), message.getUserTo());
        messageDTO.setId(relationship.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setCreateAt(message.getCreateAt());
        messageDTO.setUserFrom(userMessageDTO);
        simpMessagingTemplate.convertAndSendToUser(message.getUserTo().getEmail(), "/queue/messages", messageDTO);//Gửi tin nhắn đến userTo

    }

    public void setActive(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setIsActive(true);
        simpMessagingTemplate.convertAndSend("/queue/active", userDTO); //Gửi thông báo active đến tất cả user
    }

    public void setSeen(Relationship relationship, User user) {
        
        simpMessagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/seen", relationship.getId()); //Gửi thông báo seen đến user
    }
}
