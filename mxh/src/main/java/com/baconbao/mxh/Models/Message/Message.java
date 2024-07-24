package com.baconbao.mxh.Models.Message;

import java.time.LocalDateTime;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private Long id;
    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "from_user_id", referencedColumnName = "IdUser")
    private User userFrom;
    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "to_user_id", referencedColumnName = "IdUser")
    @JsonIgnore
    private User userTo;
    private LocalDateTime createAt;
    private String content;
    @ManyToOne(optional = false, targetEntity = Relationship.class)
    @JoinColumn(name = "relationship_id", referencedColumnName = "id")
    @JsonIgnore
    private Relationship relationship;
    private boolean isSeen;
}
