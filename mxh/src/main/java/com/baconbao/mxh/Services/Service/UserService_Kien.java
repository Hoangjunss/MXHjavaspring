package com.baconbao.mxh.Services.Service;

import com.baconbao.mxh.Models.User;

public interface UserService_Kien {
    // tạo tài khoản
    public void saveUser(User user);
    public User findByEmail(String email);
    public boolean isEmailExist(String email);
}
