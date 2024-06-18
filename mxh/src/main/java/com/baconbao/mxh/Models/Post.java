package com.baconbao.mxh.Models;

import java.time.LocalDateTime;

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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Post") 
public class Post {
    @Id
    @Column(name = "IdPost")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;
    @Column(name = "CreateAt")
    private LocalDateTime createAt;
    @Column(name = "UpdateAt")
    private LocalDateTime updateAt;
    @Column(name = "Status")
    private String status;

}
