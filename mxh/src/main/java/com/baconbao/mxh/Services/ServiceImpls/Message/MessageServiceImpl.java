package com.baconbao.mxh.Services.ServiceImpls.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Config.Socket.SocketWeb;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.Message.MessageRepository;
import com.baconbao.mxh.Services.Service.Message.MessageService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SocketWeb socketWeb;

    @Override
    public List<Message> messageFromUser(User userFrom, User userTo) {
        // Truy vấn và trả về tất cả tin nhắn giữa hai người dùng
        return messageRepository.findAllMessagesBetweenTwoUsers(userFrom, userTo);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            // Nếu tin nhắn chưa có ID, tạo một ID mới
            if (message.getId() == null) {
                message.setId(getGenerationId());
            }
            // Lưu tin nhắn vào repository
            messageRepository.save(message);
            // Gửi tin nhắn qua websocket
            socketWeb.sendMessage(message);
        } catch (DataIntegrityViolationException e) {
            // Xử lý ngoại lệ nếu không thể lưu tin nhắn
            throw new CustomException(ErrorCode.IMAGE_NOT_SAVED);
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL;
    }

    // Tìm kiếm tin nhắn theo nội dung tương tự
    // Lỗi truy vấn LIKE content
    @Override
    public List<Message> findByContent(String content) {
        try {
            return messageRepository.findByContentLike(content);
        } catch (EntityNotFoundException e) {
            // Xử lý ngoại lệ nếu không tìm thấy tin nhắn
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Message findLatestMessage(User userFrom, User userTo) {

        // Lấy tất cả tin nhắn giữa hai người dùng
        List<Message> messages = messageRepository.findAllMessagesBetweenTwoUsers(userFrom, userTo);
        // Lấy tin nhắn gần nhất
        Message message = messages.get(messages.size() - 1);

        // Trả về tin nhắn gần nhất, hoặc null nếu không có tin nhắn
        return messages.isEmpty() ? null : message;

    }

    // Loi khong xac dinh
    @Override
    @Transactional
    public void seenMessage(Relationship relationships, User user) {
        try {
            messageRepository.seenMessage(relationships, user);
            socketWeb.setSeen(relationships, user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_UPDATE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public List<Object[]> countMessageBetweenTwoUserIsSeen(User user) {
        List<Object[]> data = messageRepository.countMessageBetweenTwoUserIsSeen(user);
        return data;
    }

    @Override
    public Message findById(Long id) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            return messageOptional.get();
        }
        return null;
    }

    @Override
    public List<Object[]> countUnseenMessageByUserTo(User userTo) {
        return messageRepository.countUnseenMessageByUserTo(userTo.getId());
    }
}
