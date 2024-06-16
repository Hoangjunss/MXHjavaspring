package com.baconbao.mxh.Services.ServiceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Repository.UserRepository;
import com.baconbao.mxh.Services.Service.UserServiceKhanh;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceKhanhImpl implements UserServiceKhanh{
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public User getUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setCreateAt(userDTO.getCreateAt());
        return user;
    }

    public boolean isEmailExists(String email){
        User user = userRepository.findByEmail(email);
        return user != null;
    }
    

    @Override
    public UserDTO getUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreateAt(user.getCreateAt());
        return userDTO;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> fillAll() {
        return userRepository.findAll();
    }

}
