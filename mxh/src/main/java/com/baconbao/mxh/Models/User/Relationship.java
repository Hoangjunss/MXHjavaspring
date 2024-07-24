package com.baconbao.mxh.Models.User;

import java.util.List;

import com.baconbao.mxh.Models.Message.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "relationship")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor 
@Builder// Đặt tên bảng trong cơ sở dữ liệu
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_one_id", referencedColumnName = "IdUser")
    private User userOne;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_two_id", referencedColumnName = "IdUser")
    private User userTwo;

    @ManyToOne
    @JoinColumn(name = "idStatus")
    private StatusRelationship status;

    @OneToMany(mappedBy = "relationship",fetch = FetchType.EAGER, targetEntity = Message.class, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;
}
