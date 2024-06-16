package com.baconbao.mxh.Models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity/* thư viên dùng để tạo bảng sql */
@Table(name = "User")/* annotion dùng để tạo tên bảng trong sql */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id/* dùng để cho biết thằng nào là khóa chính */
    @GeneratedValue(strategy = GenerationType.IDENTITY)/* cho phép id tăng tự động */
    @Column(name="IdUser")
    private Long Id;
    @Column(name="LastName")
    private String LastName;
    @Column(name="FirstName")
    private String FirstName;
    @Column(name="Password")
    private String Password;
    @Column(name="Email")
    private String Email;
    @Column(name="CreateAt")
    private Date CreateAt;
}
