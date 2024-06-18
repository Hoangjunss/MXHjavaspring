package com.baconbao.mxh.Services.Service;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Models.VerifycationToken;
@Service
public interface VerifycationTokenService {
    void registerUser(User user);
    void confirmUser(Long token);
    VerifycationToken findById(Long id);
}
