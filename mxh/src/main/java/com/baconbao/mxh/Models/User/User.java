package com.baconbao.mxh.Models.User;

import java.util.Date;
import java.util.List;

import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Models.Post.Post;

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
import lombok.NoArgsConstructor;

@Entity /* thư viên dùng để tạo bảng sql */
@Table(name = "User") /* annotion dùng để tạo tên bảng trong sql */
@AllArgsConstructor
@NoArgsConstructor
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
        private Date createAt;

        private List<Relationship> userOneRelationships;
        private List<Relationship> userTwoRelationships;

        @OneToMany(mappedBy = "userOne", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        public List<Relationship> getUserOneRelationships() {
                return userOneRelationships;
        }

        public void setUserOneRelationships(List<Relationship> userOneRelationships) {
                this.userOneRelationships = userOneRelationships;
        }

        @OneToMany(mappedBy = "userTwo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        public List<Relationship> getUserTwoRelationships() {
                return userTwoRelationships;
        }

        public void setUserTwoRelationships(List<Relationship> userTwoRelationships) {
                this.userTwoRelationships = userTwoRelationships;
        }

        @OneToMany // Một user có thể có nhiều About
        @JoinTable(name = "users_about", // Tên bảng liên kết
                        joinColumns = @JoinColumn(name = "IdUser"), // Khóa ngoại của bảng User
                        inverseJoinColumns = @JoinColumn(name = "IdAbout") // Khóa ngoại của bảng About
        )
        private List<About> about;
        @OneToOne
        @JoinTable(name = "users_image", // Tên bảng liên kết
                        joinColumns = @JoinColumn(name = "IdUser"), // Khóa ngoại của bảng User
                        inverseJoinColumns = @JoinColumn(name = "IdImage") // Khóa ngoại của bảng About
        )
        private Image image;

        @OneToMany // Một user có thể có nhiều About
        @JoinTable(name = "users_post", // Tên bảng liên kết
                        joinColumns = @JoinColumn(name = "IdUser"), // Khóa ngoại của bảng User
                        inverseJoinColumns = @JoinColumn(name = "IdPost") // Khóa ngoại của bảng About
        )
        private List<Post> post;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public Date getCreateAt() {
                return createAt;
        }

        public void setCreateAt(Date createAt) {
                this.createAt = createAt;
        }

        public List<About> getAbout() {
                return about;
        }

        public void setAbout(List<About> about) {
                this.about = about;
        }

        public Image getImage() {
                return image;
        }

        public void setImage(Image image) {
                this.image = image;
        }

        public List<Post> getPost() {
                return post;
        }

        public void setPost(List<Post> post) {
                this.post = post;
        }


}
