package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Config.Socket.SocketWeb;
import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Exceptions.UserNotFoundException;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.UserRepository;
import com.baconbao.mxh.Services.Service.User.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SocketWeb socketWeb;

    public UserServiceImpl(SocketWeb socketWeb) {
        this.socketWeb = socketWeb;
    }

    // Định dạng email
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // Kiểm tra email
    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN); // kiểm tra email
    }

    // Luu user
    @Override
    public void saveUser(User username) {

        try {
            if (username.getId() == null) {
                username.setId(getGenerationId());
                username.setPassword(passwordEncoder.encode(username.getPassword())); // mã hóa mật khẩu
            }
            userRepository.save(username); // lưu user vào database
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_NOT_SAVE);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } else {
            return userRepository.findByEmail(email); // tìm kiếm user theo email
        }
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
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserNotFoundException("User not found id: " + userId);
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
        try {
            return userRepository.findAll();
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

    }

    // Kiem tra email co ton tai
    public boolean isEmailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public void setIsOnline(User user) {
        try {
            user.setIsActive(true);
            userRepository.save(user);
            socketWeb.setActive(user);
        } catch (Exception e) {

            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void setIsOffline(User user) {
        try {
            user.setIsActive(false);
            userRepository.save(user);
            socketWeb.setActive(user);
        } catch (Exception e) {

            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PostConstruct
    public void setActiveUserToFalse() {
        try {
            if (userRepository.count() > 0) {
                userRepository.updateActiveUserToFalse();
            }
        } catch (Exception e) {

            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL;
    }


    //Lỗi truy vấn like
    @Override
    public List<User> findAllByFirstNameOrLastName(String name) {
        try {
            return userRepository.findByLastNameOrFirstName(name, name);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public List<User> searchUser(String username) {
        try {
            return userRepository.searchUser(username);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    public User findByUserWithUserAbouts(Long id) {
        return userRepository.findByUserWithUserAbouts(id);
    }
}
