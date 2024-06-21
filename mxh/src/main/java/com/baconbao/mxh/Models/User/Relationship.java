package com.baconbao.mxh.Models.User;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "relationship")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // Đặt tên bảng trong cơ sở dữ liệu
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_one_id", referencedColumnName = "IdUser")
    private User userOne;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_two_id", referencedColumnName = "IdUser")
    private User userTwo;

    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "relationship", targetEntity = Message.class, cascade = CascadeType.ALL)
    private List<Message> messages;


    // Getters và Setters

}
