package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.DTO.UserDTO;

import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.UserRepository;
import com.baconbao.mxh.Services.Service.User.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Định dạng email
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // Kiểm tra email
    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN); // kiểm tra email
    }

    // Luu user
    @Override
    public void saveUser(User username) {
        User user = new User();
        long id = 0;
        if (username.getId() == null) {
            if (userRepository.countById() == null) {
                id = 1;
            } else {
                id = userRepository.countById() + 1;
            }
            if (username.getEmail() != null || isEmailValid(username.getEmail()) || username.getPassword() != null) {
                user.setId(id); // set giá trị cho biến user
                user.setFirstName(username.getFirstName()); // set giá trị cho biến user
                user.setLastName(username.getLastName()); // set giá trị cho biến user
                user.setEmail(username.getEmail()); // set giá trị cho biến user
                user.setPassword(passwordEncoder.encode(username.getPassword()));// set giá trị cho biến user
                user.setCreateAt(username.getCreateAt());
                userRepository.save(user); // lưu user vào database
            } else
                System.out.println("Email hoặc mật khẩu không hợp lệ"); // thông báo email hoặc mật khẩu không hợp lệ
        } else {
            id = username.getId();
            user.setId(id); // set giá trị cho biến user
            user.setFirstName(username.getFirstName()); // set giá trị cho biến user
            user.setLastName(username.getLastName()); // set giá trị cho biến user
            user.setEmail(username.getEmail()); // set giá trị cho biến user
            user.setPassword(username.getPassword());// set giá trị cho biến user
            user.setCreateAt(username.getCreateAt());
            user.setImage(username.getImage());
            userRepository.save(user); // lưu user vào database
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email); // tìm kiếm user theo email
    }

    @Override
    public boolean isEmailExist(String email) {
        User user = userRepository.findByEmail(email); // tìm kiếm user theo email
        if (user != null)
            return true; // thông báo email đã tồn tại
        else
            return false; // thông báo email chưa tồn tại
    }

    // Tim user bang id
    @Override
    public User findById(long userId) {
        Optional<User> user=userRepository.findById(userId); 
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

    // Chuyen userDTO ve user
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

    // Chuyen user ve userDTO
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

    // Lay danh sach user
    @Override
    public List<User> fillAll() {
        return userRepository.findAll();
    }

    // Kiem tra email co ton tai
    public boolean isEmailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public void setIsOnline(User user) {
        user.setIsActive(true);
        userRepository.save(user);
    }
}
