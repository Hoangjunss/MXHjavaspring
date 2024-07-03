package com.baconbao.mxh.Controller.Message;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baconbao.mxh.DTO.RelationshipDTO;
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
    private UserDetailsService userDetailsService;
    @Autowired
    private RelationshipService relationshipService;

    // Lấy đoạn tin nhắn của 2 user 
    @GetMapping("/send")
    public String getMessagePage(@RequestParam Long id, Model model, Principal principal) { // id là id của user cần nhắn tin với user đang login hiện tại 
        // Lấy user đang login
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user1 = userService.findByEmail(userDetails.getUsername());
        // Tìm user theo id
        User user2 = userService.findById(id);
        List<Message> listMessage = messageService.messageFromUser(user1, user2);
        model.addAttribute("listmessage", listMessage);
        return "sendmessage";
    }

    // Ở giao diện mobile - hiển thị danh sách bạn bè nhắn tin
    @GetMapping("/messagermobile")
    public String getMessagePageMobile(Model model, Principal principal) {
        // Lấy user đang login
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findByEmail(userDetails.getUsername());
        // Tìm kiếm tất cả mối quan hệ của user
        List<Relationship> relationships = relationshipService.findAllByUserOneId(user);
        // Tạo đối tượng DTO để lưu những thông tin cần.
        List<RelationshipDTO> relationshipDTOs = new ArrayList<>();
        // Tạo mảng danh sách tin nhắn
        // Duyệt qua tất cả mối quan hệ
        for (Relationship relationship : relationships) {
            // Kiểm tra nếu có tin nhắn trong mối quan hệ thì lấy tin nhắn cuối cùng của mối quan hệ
            if (relationship.getMessages() != null) {
                int count;
                // Nếu user đang login nằm ở userONE
                if (relationship.getUserOne().getId() == user.getId()) {
                    count = messageService.CountMessageBetweenTwoUserIsSeen(relationship.getUserTwo(), relationship.getUserOne());
                    // Lấy tin nhắn gần với hiện tại nhất của mối quan hệ
                    Message message = messageService.findLatestMessage(user, relationship.getUserTwo());
                    // Tạo đối tượng DTO để lưu những thông tin cần thiết: id mối quan hệ, id user thứ 2, tên user thứ 2, nội dung tin nhắn, ngày nhắn.
                    RelationshipDTO dto = new RelationshipDTO(relationship.getId(), relationship.getUserTwo().getId(),
                            relationship.getUserTwo().getLastName() + " " + relationship.getUserTwo().getFirstName(),
                            message.getContent(), count, message.getCreateAt());
                    // Lưu vào danh sách DTO
                    relationshipDTOs.add(dto);
                } 
                // Tương tự nếu user đang login nằm ở userTWO của mối quan hệ
                else if (relationship.getUserTwo().getId() == user.getId()) {
                    count = messageService.CountMessageBetweenTwoUserIsSeen(relationship.getUserOne(), relationship.getUserTwo());
                    Message message = messageService.findLatestMessage(user, relationship.getUserOne());
                    RelationshipDTO dto = new RelationshipDTO(relationship.getId(), relationship.getUserOne().getId(),
                            relationship.getUserOne().getLastName() + " " + relationship.getUserOne().getFirstName(),
                            message.getContent(), count, message.getCreateAt());
                    relationshipDTOs.add(dto);
                }
            } else {
                if (relationship.getUserOne().getId() == user.getId()) {
                    RelationshipDTO dto = new RelationshipDTO(relationship.getId(), relationship.getUserTwo().getId(),
                            relationship.getUserTwo().getLastName() + " " + relationship.getUserTwo().getFirstName(),
                            "", 0, null);
                    relationshipDTOs.add(dto);
                } else if (relationship.getUserTwo().getId() == user.getId()) {
                    RelationshipDTO dto = new RelationshipDTO(relationship.getId(), relationship.getUserOne().getId(),
                            relationship.getUserOne().getLastName() + " " + relationship.getUserOne().getFirstName(),
                            "", 0, null);
                    relationshipDTOs.add(dto);
                }
            }
        }
        // Sắp xếp danh sách theo ngày nhắn gần nhất
        relationshipDTOs = relationshipService.orderByCreateAt(relationshipDTOs);
        // Lưu vào model
        model.addAttribute("relationships", relationshipDTOs);
        return "/User/Message/Mobile/Message"; // Dòng này cần dấu chấm phẩy
    }
    
    // Ở giao diện mobile - lấy đoạn tin nhắn cụ thể của 2 user
    @GetMapping("/chatmobile")
    public String getChatPageMobile(@RequestParam Long id, Model model, Principal principal) {
        // Lấy user đang login
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User userFrom = userService.findByEmail(userDetails.getUsername());
        // Tìm user theo id
        User userTo = userService.findById(id);
        // Lấy Danh sách tin nhắn của 2 bên
        List<Message> listMessage = messageService.messageFromUser(userFrom, userTo);
        Relationship relationship = relationshipService.findRelationship(userFrom, userTo);
        messageService.seenMessage(relationship, userFrom);
        // Trả về html những thôngtin cần thiết để hiển thị
        model.addAttribute("messages", listMessage);
        model.addAttribute("relation", relationship);
        model.addAttribute("userTo", userTo);
        model.addAttribute("currentUser", userFrom); // Add the current user to the model
        return "/User/Message/Mobile/Chat";
    }

    // Ở giao diện website - lấy danh sách quan hệ và hiển thị đoạn chat của người
    // gần nhất
    @GetMapping("/messager")
    public String getMessagePage(Model model, Principal principal) {
        // Lấy user đang login
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findByEmail(userDetails.getUsername());
        // Tìm kiếm tất cả mối quan hệ của user
        List<Relationship> relationships = relationshipService.findAllByUserOneId(user);
        // Tạo đối tượng DTO để lưu những thông tin cần.
        List<RelationshipDTO> relationshipDTOs = new ArrayList<>();
        // Duyệt qua tất cả mối quan hệ
        for (Relationship relationship : relationships) {
            // Kiểm tra nếu có tin nhắn trong mối quan hệ thì lấy tin nhắn cuối cùng của mối
            // quan hệ
            if (relationship.getMessages() != null) {
                int count ;
                // Nếu user đang login nằm ở userONE của mối quan hệ thì lấy tin nhắn gần với
                // hiện tại nhất của mối quan hệ
                if (relationship.getUserOne().getId() == user.getId()) {
                    count= messageService.CountMessageBetweenTwoUserIsSeen(relationship.getUserTwo(),
                    relationship.getUserOne());
                    Message message = messageService.findLatestMessage(user, relationship.getUserTwo());
                    RelationshipDTO dto = new RelationshipDTO(relationship.getId(), relationship.getUserTwo().getId(),
                            relationship.getUserTwo().getLastName() + " " + relationship.getUserTwo().getFirstName(),
                            message.getContent(), count, message.getCreateAt());
                    relationshipDTOs.add(dto);
                }
                // Tương tự userTwo
                else if (relationship.getUserTwo().getId() == user.getId()) {
                    count=messageService.CountMessageBetweenTwoUserIsSeen(relationship.getUserOne(),
                    relationship.getUserTwo());
                    Message message = messageService.findLatestMessage(user, relationship.getUserOne());
                    RelationshipDTO dto = new RelationshipDTO(relationship.getId(), relationship.getUserOne().getId(),
                            relationship.getUserOne().getLastName() + " " + relationship.getUserOne().getFirstName(),
                            message.getContent(), count, message.getCreateAt());
                    relationshipDTOs.add(dto);
                }
            }
            
        }
        // Sắp xếp danh sách theo ngày nhắn gần nhất
        relationshipDTOs = relationshipService.orderByCreateAt(relationshipDTOs);
        // Từ danh sách đã sắp xếp lấy phần tử đầu tiên của danh sách để tìm quan hệ
        Relationship relationship = relationshipService.findById(relationshipDTOs.get(0).getId());
        // Từ quan hệ tìm tiếp đoạn tin nhắn của quan hệ đó
        List<Message> listMessage = messageService.messageFromUser(relationship.getUserOne(),
                relationship.getUserTwo());
        // Trả về html những thôngtin cần thiết để hiển thị
        model.addAttribute("listmessage", listMessage);
        model.addAttribute("relationships", relationshipDTOs);
        // Kiểm tra trong mối quan hệ userlogin ở vị trí one hay two để hiển thị thông
        // tin của người đối diện đang chat
        if (user.getId() == relationship.getUserOne().getId()) {
            model.addAttribute("userTo", relationship.getUserTwo());
        } else {
            model.addAttribute("userTo", relationship.getUserOne());
        }
        model.addAttribute("relation", relationship);
        model.addAttribute("currentUser", user); // Add the current user to the model
        return "/User/Message/Web/Messager";
    }

    // Ở giao diện website - phản hồi dưới dạng JSON cho ajax
    @PostMapping("/chat")
    public ResponseEntity<?> getChatMessages(@RequestParam("id") Long userId, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Lấy thông tin chi tiết của người dùng hiện tại từ principal
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            // Tìm người dùng hiện tại từ email của họ
            User currentUser = userService.findByEmail(userDetails.getUsername());
            // Tìm người dùng chat từ userId đã được truyền vào request
            User chatUser = userService.findById(userId);
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
        } catch (Exception e) {
            // Nếu có lỗi, đặt thông báo lỗi vào phản hồi và trả về HTTP 500
            response.put("error", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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

        Message messages = new Message();
        messages.setContent(content);

        // Chuyển đổi recipient thành Long
        User user = userService.findById(Long.parseLong(id));
        User userFrom = userService.findByEmail(principal.getName());

        messages.setUserFrom(userFrom);
        messages.setUserTo(user);
        messages.setCreateAt(LocalDateTime.now());

        // Giả sử relationshipService.findById trả về một đối tượng Relationship
        Relationship relationship = relationshipService.findRelationship(user, userFrom);
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

    @PostMapping("/searchmessage")
    public ResponseEntity<?> searchMessage(@RequestBody String name) {
        try {
            List<Message> messages = new ArrayList<>();
            messages = messageService.findByContent(name);
            return ResponseEntity.ok(messages);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
