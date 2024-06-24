package com.baconbao.mxh.Models.Post;

import java.time.LocalDateTime;
import java.util.List;

import com.baconbao.mxh.Models.User.User;

import jakarta.persistence.*;
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
        private Long id;
        @Column(name = "Content", columnDefinition = "TEXT")
        private String content;
        @Column(name = "CreateAt")
        private LocalDateTime createAt;
        @Column(name = "UpdateAt")
        private LocalDateTime updateAt;
        private boolean isActive;
        @OneToMany
        @JoinTable(name = "post_comment", // Tên bảng liên kết
        joinColumns = @JoinColumn(name = "IdPost"), // Khóa ngoại của bảng User
        inverseJoinColumns = @JoinColumn(name = "idComment") // Khóa ngoại của bảng About
)
        private List<Comment> comments;
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

        @OneToMany
        @JoinTable(name ="post_interaction")
        private List<Interaction> interactions;

}
