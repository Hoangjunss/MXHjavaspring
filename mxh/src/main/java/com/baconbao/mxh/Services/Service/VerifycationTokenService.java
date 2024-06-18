package com.baconbao.mxh.Services.Service;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User;
@Service
public interface VerifycationTokenService {
    void registerUser(User user);
    void confirmUser(Long token);

}
