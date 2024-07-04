package com.baconbao.mxh.DTO;

import java.time.LocalDateTime;

import com.baconbao.mxh.Models.User.Relationship;

public class RelationshipDTO {
    private Long id;
    private Long idUser;
    private String name;
    private String content;
    private int countMessageNotSeen;
    private LocalDateTime createAt;

    public RelationshipDTO(){}

    // Constructor
    public RelationshipDTO(Long id, Long idUser, String name, String content, int countMessageNotSeen, LocalDateTime createAt) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.content = content;
        this.countMessageNotSeen = countMessageNotSeen;
        this.createAt = createAt;
    }

    public RelationshipDTO(Relationship relationship){
        this.id = relationship.getId();
        this.idUser = relationship.getUserTwo().getId();
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

    public int getCountMessageNotSeen() {
        return countMessageNotSeen;
    }

    public void setCountMessageNotSeen(int countMessageNotSeen) {
        this.countMessageNotSeen = countMessageNotSeen;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
    

}
