package com.baconbao.mxh.Models.Message;

import lombok.Builder;

@Builder
public class ChatMessage {
    private Long id;
    private String content;
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
    
}
