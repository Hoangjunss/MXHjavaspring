package com.baconbao.mxh.Services.ServiceImpls;

import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Services.Service.UserService_Kien;

public class UserService_KienImpl implements UserService_Kien{

    @Override
    public User createUser(User user) {
        if (user != null) {
            return user;
        } else 
            return null;
    }

    @Override
    public boolean isEmailExist(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEmailExist'");
    }

    @Override
    public boolean isNameExist(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isNameExist'");
    }

}
