package com.baconbao.mxh.Controller.Message;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.Service.Message.MessageService;
import com.baconbao.mxh.Services.Service.User.RelationshipService;
import com.baconbao.mxh.Services.Service.User.UserService;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private RelationshipService relationshipService;

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

    @PostMapping("/searchmessage")
    public ResponseEntity<?> searchMessage(@RequestBody String name) {
        try {
            System.out.println(name + " Key search");
            List<Message> messages = new ArrayList<>();
            messages = messageService.findByContent(name);
            if (messages.isEmpty()) {
                System.out.println("No messages found.");
            }
            for (Message message : messages) {
                System.out.println(message.getContent() + " SEARCH MESSAGE");
            }
            return ResponseEntity.ok(messages);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
