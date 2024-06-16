package com.baconbao.mxh.DTO;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long Id;
    private String LastName;
    private String FirstName;
    private String Password;
    private String Email;
    private Date CreateAt;
}