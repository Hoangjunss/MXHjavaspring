package com.baconbao.mxh.Controller.Message;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baconbao.mxh.Models.Message.ChatMessage;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.Service.Message.MessageService;
import com.baconbao.mxh.Services.Service.User.RelationshipService;
import com.baconbao.mxh.Services.Service.User.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/send")
    public String getMessagePage(@RequestParam Long id, Model model, Principal principal) {
        User user2 = userService.findById(id);
        User user1 = userService.findByEmail(principal.getName());
        List<Message> listMessage = messageService.messageFromUser(user1, user2);
        model.addAttribute("listmessage", listMessage);
        return "sendmessage";
    }

    @MessageMapping("/chat.send")
    @SendTo("/queue/messages")
    public Message sendMessage(@Payload Map<String, Object> message, Principal principal) {

        Map<String, Object> innerMessage = (Map<String, Object>) message.get("message");
        String content = (String) innerMessage.get("content");
        String id = String.valueOf(innerMessage.get("id"));

        Message messages = new Message();
        messages.setContent(content);

        // Chuyển đổi recipient thành Long
        User user = userService.findById(Long.parseLong(id));
        User userFrom = userService.findByEmail(principal.getName());

        messages.setUserFrom(userFrom);
        messages.setUserTo(user);
        messages.setCreateAt(LocalDateTime.now());

        // Giả sử relationshipService.findById trả về một đối tượng Relationship
        Relationship relationship = relationshipService.findById(1L);
        messages.setRelationship(relationship);

        List<Message> messagesList = new ArrayList<>();
        messagesList.add(messages);

        messageService.sendMessage(messages);

        user.setToUserMessagesList(messagesList);
        userFrom.setFromUserMessagesList(messagesList);
        userService.saveUser(user);
        userService.saveUser(userFrom);

        return messages;
    }
}
