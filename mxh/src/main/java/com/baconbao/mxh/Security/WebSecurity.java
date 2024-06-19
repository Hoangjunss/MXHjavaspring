package com.baconbao.mxh.Security;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Services.Service.UserService;

public class WebSecurity implements UserDetailsService {
    @Autowired
    private UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userService.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }else{
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(),
            getAuthorities());
        }
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }
    
}
