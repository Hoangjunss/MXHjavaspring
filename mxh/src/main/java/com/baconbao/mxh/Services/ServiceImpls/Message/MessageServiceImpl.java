package com.baconbao.mxh.Services.ServiceImpls.Message;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Config.Socket.SocketWeb;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.Message.MessageRepository;
import com.baconbao.mxh.Services.Service.Message.MessageService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private SocketWeb socketWeb;

    @Override
    public List<Message> messageFromUser(User userFrom, User userTo) {
        return messageRepository.findAllMessagesBetweenTwoUsers(userFrom, userTo);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            if(message.getId() == null){
                message.setId(getGenerationId());
            }
            messageRepository.save(message);
            socketWeb.sendMessage(message);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.IMAGE_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }


    //Lỗi truy vấn LIKE content
    @Override
    public List<Message> findByContent(String content) {
        try {
            return messageRepository.findByContentLike(content);
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } catch (Exception e){
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public Message findLatestMessage(User userFrom, User userTo) {
        try {
            List<Message> messages = messageRepository.findAllMessagesBetweenTwoUsers(userFrom, userTo);
            Message message = messages.get(messages.size() - 1);

        // Trả về tin nhắn gần nhất
        return messages.isEmpty() ? null : message;
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        } catch (Exception e){
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }


}
