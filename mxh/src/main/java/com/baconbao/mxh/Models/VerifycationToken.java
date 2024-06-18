package com.baconbao.mxh.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifycationToken {
    @Id
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private LocalDateTime setExpiryDate;
    
}
