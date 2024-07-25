package com.baconbao.mxh.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.StatusRelationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.Message.MessageRepository;
import com.baconbao.mxh.Services.Service.Message.MessageService;

@SpringBootTest
public class MessageServiceTest {
    @Autowired
    private MessageService messageService;
    @MockBean
    private MessageRepository messageRepository;

    private Message message;
    private User userFrom;
    private User userTo;
    private StatusRelationship status;
    private Relationship relationship;

    @BeforeEach
    void initData() {
        userFrom = new User(1L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        userTo = new User(2L, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        status = new StatusRelationship(1L, "nguoila");
        relationship = new Relationship(1L, userTo, userTo, status, null);
        
        message = Message.builder()
                .id(1L)
                .content("Hello")
                .userFrom(userFrom)
                .userTo(userTo)
                .relationship(relationship)
                .build();
    }

    @Test
    public void sendMessage_Saved_ThrowsException() throws Exception {
        messageService.sendMessage(message);
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    public void countMessageBetweenTwoUserIsSeen_ThrowsException() throws Exception {
        List<Object[]> messages = new ArrayList<>();
        when(messageRepository.countMessageBetweenTwoUserIsSeen(userFrom)).thenReturn(messages);
        Assertions.assertThatException();
    }

    @Test
    public void countUnseenMessageByUserTo_ThrowsException() throws Exception {
        List<Object[]> messages = new ArrayList<>();
        when(messageRepository.countUnseenMessageByUserTo(2L)).thenReturn(messages);
        Assertions.assertThatException();
    }

}
