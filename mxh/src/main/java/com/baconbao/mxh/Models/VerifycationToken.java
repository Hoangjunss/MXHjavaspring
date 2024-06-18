package com.baconbao.mxh.Models;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifycationToken {
  
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private Date setExpiryDate;
    
}
