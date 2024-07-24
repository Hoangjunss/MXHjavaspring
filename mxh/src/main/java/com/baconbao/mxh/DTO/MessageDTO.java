package com.baconbao.mxh.DTO;

import java.time.LocalDateTime;

import lombok.Builder;
@Builder
public class MessageDTO {
    private Long id;
    private String content;
    private LocalDateTime createAt;
    private UserMessageDTO userFrom;
    public MessageDTO() {
    }
    public MessageDTO(Long id, String content, LocalDateTime createAt, UserMessageDTO userFrom) {
        this.id = id;
        this.content = content;
        this.createAt = createAt;
        this.userFrom = userFrom;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }
    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
    public UserMessageDTO getUserFrom() {
        return userFrom;
    }
    public void setUserFrom(UserMessageDTO userFrom) {
        this.userFrom = userFrom;
    }
    
}
