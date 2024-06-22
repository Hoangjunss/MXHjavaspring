package com.baconbao.mxh.DTO;

import com.baconbao.mxh.Models.User.User;

public class UserMessageDTO {
    private Long id;
    private String name;
    
    public UserMessageDTO() {
    }
    public UserMessageDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserMessageDTO(User user){
        this.id = user.getId();
        this.name = user.getFirstName()+" "+user.getLastName();

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    
    
}
