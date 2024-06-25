package com.baconbao.mxh.Services.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Models.User;

@Service
public interface UserServiceKhanh {
    List<User> fillAll();
    User findById(long userId);
    User getUser(UserDTO userDTO);
    void saveUser(User user);
    UserDTO getUserDTO(User user);
}
