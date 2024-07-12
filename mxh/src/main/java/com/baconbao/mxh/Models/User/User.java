package com.baconbao.mxh.Models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.baconbao.mxh.Models.Message.Message;
import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Models.Post.Interaction;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.Post.ReplyComment;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity /* thư viên dùng để tạo bảng sql */
@Table(name = "User") /* annotion dùng để tạo tên bảng trong sql */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
        @Id
        @Column(name = "IdUser")
        private Long id;
        @Column(name = "LastName")
        private String lastName;
        @Column(name = "FirstName")
        private String firstName;
        @Column(name = "Password")
        private String password;
        @Column(name = "Email")
        private String email;
        @Column(name = "CreateAt")
        private LocalDateTime createAt;
        private Boolean isActive;
        @OneToMany(mappedBy = "userOne", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JsonIgnore
        private List<Relationship> userOneRelationships = new ArrayList<>();
        @OneToMany(mappedBy = "userTwo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        @JsonIgnore
        private List<Relationship> userTwoRelationships = new ArrayList<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<UserAbout> userAbouts = new ArrayList<>();
        @OneToMany(mappedBy = "userFrom", cascade = CascadeType.ALL)
        @JsonIgnore
        private List<Message> fromUserMessagesList;
        @OneToMany(mappedBy = "userTo", cascade = CascadeType.ALL)
        @JsonIgnore
        private List<Message> toUserMessagesList;
        @OneToMany(mappedBy = "userSend")
        @JsonIgnore
        private List<Comment> comments;

        @OneToOne(fetch = FetchType.EAGER)
        @JsonIgnore
        @JoinTable(name = "users_image", // Tên bảng liên kết
                        joinColumns = @JoinColumn(name = "IdUser"), // Khóa ngoại của bảng User
                        inverseJoinColumns = @JoinColumn(name = "IdImage") // Khóa ngoại của bảng About
        )
        private Image image;

        @OneToMany // Một user có thể có nhiều About
        @JsonIgnore
        @JoinTable(name = "users_post", // Tên bảng liên kết
                        joinColumns = @JoinColumn(name = "IdUser"), // Khóa ngoại của bảng User
                        inverseJoinColumns = @JoinColumn(name = "IdPost") // Khóa ngoại của bảng About
        )
        private List<Post> post;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<Interaction> interactions = new ArrayList<>();

        @OneToMany(mappedBy = "userSend")
        @JsonIgnore
        private List<ReplyComment> replyComments;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        private List<Notification> notifications;
}