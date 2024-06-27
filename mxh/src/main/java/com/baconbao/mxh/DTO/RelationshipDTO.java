package com.baconbao.mxh.DTO;

import java.time.LocalDateTime;

public class RelationshipDTO {
    private Long id;
    private Long idUser;
    private String name;
    private String content;
    private LocalDateTime createAt;
    
    public RelationshipDTO() {
    }
    
    public RelationshipDTO(Long id, Long idUser, String name, String content, LocalDateTime createAt) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.content = content;
        this.createAt = createAt;
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

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
    

}
