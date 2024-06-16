package com.baconbao.mxh.Services.Service;

import java.util.List;

import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Models.User;

public interface UserService {

    User findByEmail(String email);

    boolean isEmailExist(String email);

    List<User> fillAll();

    User findById(long userId);

    User getUser(UserDTO userDTO);

    void saveUser(User user);

    UserDTO getUserDTO(User user);
}
