package com.baconbao.mxh.Services.ServiceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Repository.UserRepository;
import com.baconbao.mxh.Services.Service.UserService_Kien;
@Service // annotion dùng để chú thích đây là 1 service
public class UserService_KienImpl implements UserService_Kien{

    @Autowired
    private UserRepository userRepository;

    // Định dạng email
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; // định dạng email

    // Kiểm tra email
    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN); // kiểm tra email
    }



    @Override
    public void saveUser(User username) {
        User user = new User();


        if(username.getEmail() != null || isEmailValid(username.getEmail()) || username.getPassword() != null){
            user.setFirstName(username.getFirstName()); // set giá trị cho biến user
            user.setLastName(username.getLastName()); // set giá trị cho biến user
            user.setEmail(username.getEmail()); // set giá trị cho biến user
            user.setPassword(username.getPassword()); // set giá trị cho biến user
            userRepository.save(user); // lưu user vào database
        }
        else
            System.out.println("Email hoặc mật khẩu không hợp lệ"); // thông báo email hoặc mật khẩu không hợp lệ
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email); // tìm kiếm user theo email
    }

    @Override
    public boolean isEmailExist(String email) {
        User user = userRepository.findByEmail(email); // tìm kiếm user theo email
        if(user != null)
            return true; // thông báo email đã tồn tại
        else
            return false; // thông báo email chưa tồn tại
    }





    

}
