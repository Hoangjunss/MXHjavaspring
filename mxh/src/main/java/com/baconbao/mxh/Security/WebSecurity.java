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
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.Service.User.UserService;

@Service
public class WebSecurity implements UserDetailsService {
    @Autowired
    private UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // throws UsernameNotFoundException là một exception được ném ra khi không tìm thấy người dùng với tên người dùng đã cung cấp.
        User user = userService.findByEmail(username);
        if(user==null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }else{
            return new org.springframework.security.core.userdetails.User(user.getEmail(),//getUer
            user.getPassword(),
            getAuthorities()); // chức vụ tài khoản
        }
    }
    
    // Chữ "USER" là string nên phải chuyển thành collection
    // tại sao phải chuyển thành collection? vì phương thức getAuthorities() trả về một collection của các quyền được cấp cho người dùng.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }
    
}
