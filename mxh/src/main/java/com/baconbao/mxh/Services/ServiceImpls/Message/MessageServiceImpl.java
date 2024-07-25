package com.baconbao.mxh.Services.ServiceImpls.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SocketWeb socketWeb;

    @Override
    public List<Message> messageFromUser(User userFrom, User userTo) {
        try {
            log.info("Finding message between userfrom by id: {} and userto by id: {}" + userFrom.getId(),
                    userTo.getId());
            return messageRepository.findAllMessagesBetweenTwoUsers(userFrom, userTo);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public void sendMessage(Message message) {
        try {
            log.info("Sending message");
            if (message.getId() == null) {
                message.setId(getGenerationId());
            }
            messageRepository.save(message);
            socketWeb.sendMessage(message);
        } catch (DataIntegrityViolationException e) {
            log.info("Unable to send message");
            throw new CustomException(ErrorCode.MESSAGE_UNABLE_TO_SAVE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    // Tìm kiếm tin nhắn theo nội dung tương tự
    // Lỗi truy vấn LIKE content
    @Override
    public List<Message> findByContent(String content) {
        try {
            log.info("Finding message by content: {}", content);
            return messageRepository.findByContentLike(content);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Message findLatestMessage(User userFrom, User userTo) {
        log.info("Find latest message");
        List<Message> messages = messageRepository.findAllMessagesBetweenTwoUsers(userFrom, userTo);
        Message message = messages.get(messages.size() - 1);
        return messages.isEmpty() ? null : message;
    }

    // Loi khong xac dinh
    @Override
    @Transactional
    public void seenMessage(Relationship relationships, User user) {
        try {
            log.info("Seen message");
            messageRepository.seenMessage(relationships, user);
            socketWeb.setSeen(relationships, user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.MESSAGE_UNABLE_TO_UPDATE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public List<Object[]> countMessageBetweenTwoUserIsSeen(User user) {
        try {
            log.info("Count unseen message by user id: {}", user.getId());
            List<Object[]> data = messageRepository.countMessageBetweenTwoUserIsSeen(user);
            return data;
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Message findById(Long id) {
        log.info("Find message by id: {}", id);
        Optional<Message> messageOptional = messageRepository.findById(id);
        return messageOptional.isPresent() ? messageOptional.get() : null;
    }

    @Override
    public List<Object[]> countUnseenMessageByUserTo(User userTo) {
        try {
            return messageRepository.countUnseenMessageByUserTo(userTo.getId());
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
