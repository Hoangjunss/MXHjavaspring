package com.baconbao.mxh.Config.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.baconbao.mxh.DTO.MessageDTO;
import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.DTO.UserMessageDTO;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Notification;
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
        UserMessageDTO userMessageDTO = new UserMessageDTO(message.getUserFrom());
        Relationship relationship = relationshipService.findRelationship(message.getUserFrom(), message.getUserTo());
        MessageDTO messageDTO = MessageDTO.builder()
                                          .id(relationship.getId())
                                          .content(message.getContent())
                                          .createAt(message.getCreateAt())
                                          .userFrom(userMessageDTO)
                                          .build();
        simpMessagingTemplate.convertAndSendToUser(message.getUserTo().getEmail(), "/queue/messages", messageDTO);//Gửi tin nhắn đến userTo
    }

    public void setActive(User user) {
        UserDTO userDTO =UserDTO.builder()
                                .id(user.getId())
                                .isActive(true)
                                .build();
        simpMessagingTemplate.convertAndSend("/queue/active", userDTO); //Gửi thông báo active đến tất cả user
    }

    public void setSeen(Relationship relationship, User user) {
        
        simpMessagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/seen", relationship.getId()); //Gửi thông báo seen đến user
    }

    public void sendFriendRequestNotification(Notification notification) {
        simpMessagingTemplate.convertAndSendToUser(notification.getUser().getEmail(), "/queue/addfriend", notification);
    }
}
