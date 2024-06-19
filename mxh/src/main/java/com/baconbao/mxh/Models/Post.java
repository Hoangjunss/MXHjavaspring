package com.baconbao.mxh.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Post")
public class Post {
    @Id
    @Column(name = "IdPost")
    private long id;
    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "CreateAt")
    private LocalDateTime createAt;
    @Column(name = "UpdateAt")
    private LocalDateTime updateAt;
    @ManyToOne
    @JoinTable(name = "post_status", // Tên bảng liên kết
            joinColumns = @JoinColumn(name = "IdPost"), // Khóa ngoại của bảng User
            inverseJoinColumns = @JoinColumn(name = "idStatus") // Khóa ngoại của bảng About
    )
    private Status status;

    @OneToOne
    @JoinTable(name = "post_image", // Tên bảng liên kết
            joinColumns = @JoinColumn(name = "IdPost"), // Khóa ngoại của bảng User
            inverseJoinColumns = @JoinColumn(name = "id") // Khóa ngoại của bảng About
    )
    private Image image;
    @ManyToOne
    @JoinTable(name = "post_user", // Tên bảng liên kết
    joinColumns = @JoinColumn(name = "IdPost"), // Khóa ngoại của bảng User
    inverseJoinColumns = @JoinColumn(name = "IdUser") // Khóa ngoại của bảng About
)
private User user;
}
