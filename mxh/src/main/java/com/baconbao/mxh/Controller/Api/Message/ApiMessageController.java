package com.baconbao.mxh.Controller.Api.Message;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baconbao.mxh.Config.Web.AppConstants;
import com.baconbao.mxh.DTO.ApiResponse;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.UserNotFoundException;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.Service.Message.MessageService;
import com.baconbao.mxh.Services.Service.User.RelationshipService;
import com.baconbao.mxh.Services.Service.User.UserService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor

public class ApiMessageController {
    private MessageService messageService;
    private UserService userService;
    private UserDetailsService userDetailsService;
    private RelationshipService relationshipService;

    // Ở giao diện website - phản hồi dưới dạng JSON cho ajax
    @PostMapping("/api/chat")
    public ResponseEntity<?> getChatMessages(@RequestParam("id") Long userId, Principal principal) {
        Map<String, Object> response = new HashMap<>();
       
            // Lấy thông tin chi tiết của người dùng hiện tại từ principal
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            // Tìm người dùng hiện tại từ email của họ
            User currentUser = userService.findByEmail(userDetails.getUsername());
            

        User chatUser = userService.findById(userId);

            // Tìm người dùng chat từ userId đã được truyền vào request
            
            // Lấy danh sách tin nhắn giữa người dùng hiện tại và người dùng chat
            List<Message> messages = messageService.messageFromUser(currentUser, chatUser);
            // Đặt danh sách tin nhắn, người dùng hiện tại và người dùng chat vào phản hồi
            Relationship relationship = relationshipService.findRelationship(chatUser, currentUser);
            response.put("messages", messages);
            response.put("currentUser", currentUser);
            response.put("chatUser", chatUser);
            response.put("relationship", relationship);
            // Trả về phản hồi HTTP 200 với dữ liệu JSON
            return ResponseEntity.ok(response);
       
    }

    //
    @MessageMapping("/chat.send")
    @SendTo("/queue/messages")
    public Message sendMessage(@Payload Map<String, Object> message, Principal principal) {

        Map<String, Object> innerMessage = (Map<String, Object>) message.get("message");
        String content = "";
        String id = "";
        if (innerMessage.get("content") != null) {
            content = (String) innerMessage.get("content");
        }
        if (innerMessage.get("id") != null) {
            id = String.valueOf(innerMessage.get("id"));
        }
        User user = userService.findById(Long.parseLong(id));
        User userFrom = userService.findByEmail(principal.getName());
        Relationship relationship = relationshipService.findRelationship(user, userFrom);
        Message messages = Message.builder()
                                  .content(content)
                                  .userFrom(userFrom)
                                  .userTo(user)
                                  .createAt(LocalDateTime.now())
                                  .relationship(relationship)
                                  .build();
        List<Message> messagesList = new ArrayList<>();
        messagesList.add(messages);
        messageService.sendMessage(messages);
        user.setToUserMessagesList(messagesList);
        userFrom.setFromUserMessagesList(messagesList);
        userService.saveUser(user);
        userService.saveUser(userFrom);
        return messages;
    }

    @MessageMapping("/chat.seen")
    @SendTo("/queue/seen")
    public void sendSeen(@Payload Map<String, Object> message, Principal principal) {
        Map<String, Object> innerMessage = (Map<String, Object>) message.get("message");
        String id = String.valueOf(innerMessage.get("id"));
        // Lấy thông tin chi tiết của người dùng hiện tại từ principal
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        // Tìm người dùng hiện tại từ email của họ
        User currentUser = userService.findByEmail(userDetails.getUsername());
        Relationship relationship = relationshipService.findById(Long.parseLong(id));
        messageService.seenMessage(relationship, currentUser);
    }

    @GetMapping("/api/messagermobile")
    public ResponseEntity<?> mmobile(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        
            // Lấy thông tin chi tiết của người dùng hiện tại từ principal
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            // Tìm người dùng hiện tại từ email của họ
            User currentUser = userService.findByEmail(userDetails.getUsername());
            // Tìm kiếm tất cả mối quan hệ của user
            List<Relationship> relationships = relationshipService.findAllByUserOneId(currentUser);
            List<Object[]> countUnseen = messageService.countUnseenMessageByUserTo(currentUser);
            List<Object[]> countMessNotSeen = messageService.countMessageBetweenTwoUserIsSeen(currentUser);
            response.put("countMessNotSeen", countMessNotSeen);
            response.put("user", currentUser);
            response.put("relantionships", relationships);
            response.put("unseenMessages", countUnseen);
            return ResponseEntity.ok(response);
       
    }

    @GetMapping("/api/chat")
    public ResponseEntity<?> mobile(@RequestParam("id") Long userId, Principal principal) {
        Map<String, Object> response = new HashMap<>();
       
            // Lấy thông tin chi tiết của người dùng hiện tại từ principal
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            // Tìm người dùng hiện tại từ email của họ
            User userFrom = userService.findByEmail(userDetails.getUsername());
            // Tìm kiếm tất cả mối quan hệ của user
            // Tìm user theo id
            User userTo = userService.findById(userId);
            // Lấy Danh sách tin nhắn của 2 bên
            List<Message> listMessage = messageService.messageFromUser(userFrom, userTo);
            Relationship relationship = relationshipService.findRelationship(userFrom, userTo);
            messageService.seenMessage(relationship, userFrom);
            response.put("messages", listMessage);
            response.put("relation", relationship);
            response.put("userTo", userTo);
            response.put("currentUser", userFrom);

            return ResponseEntity.ok(response);
     
    }

}
