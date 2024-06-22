package com.baconbao.mxh.Config.Web;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.Service.User.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public CustomAuthenticationSuccessHandler(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                String username = authentication.getName();

                // Tải thông tin người dùng bằng UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                User user=userService.findByEmail(userDetails.getUsername());
                userService.setIsOnline(user);
                response.sendRedirect("/");
            }
    
}
