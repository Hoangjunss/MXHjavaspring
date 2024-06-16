package com.baconbao.mxh.Services.Service;

import com.baconbao.mxh.Models.User;

public interface UserService_Kien {
    // tạo tài khoản
    public User createUser(User user);
    public boolean isEmailExist(String email);
    public boolean isNameExist(String name);
    
}
