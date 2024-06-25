package com.baconbao.mxh.Services.Service;

import java.util.List;

import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Models.User;

public interface UserService {
    List<User> fillAll();
    User findById(long userId);
    void saveUser(User user);
    User findByEmail(String email);
    User getUser(UserDTO userDTO);
    UserDTO getUserDTO(User user);
    boolean isEmailExist(String email);
}
