package com.baconbao.mxh.Models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity /* thư viên dùng để tạo bảng sql */
@Table(name = "User") /* annotion dùng để tạo tên bảng trong sql */
@Getter
@Setter
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
    @OneToMany // Một user có thể có nhiều About
    @JoinTable(name = "users_about", // Tên bảng liên kết
            joinColumns = @JoinColumn(name = "IdUser"), // Khóa ngoại của bảng User
            inverseJoinColumns = @JoinColumn(name = "IdAbout") // Khóa ngoại của bảng Role
    )
    private List<About> about; 
}
