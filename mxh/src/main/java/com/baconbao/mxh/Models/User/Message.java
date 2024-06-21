package com.baconbao.mxh.Models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private Long id;
     @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "from_user_id", referencedColumnName = "IdUser")
    private User userFrom;
    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "from_user_id", referencedColumnName = "IdUser")
    private User userTo;
    private LocalDateTime createAt;
    private String content;
    @ManyToOne(optional = false, targetEntity = Relationship.class)
    @JoinColumn(name = "relationship_id", referencedColumnName = "id")
    private Relationship relationship;
}
