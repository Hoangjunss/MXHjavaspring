package com.baconbao.mxh.Services.ServiceImpls.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Config.Socket.SocketWeb;
import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.UserRepository;
import com.baconbao.mxh.Services.Service.Post.ImageService;
import com.baconbao.mxh.Services.Service.User.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.QueryTimeoutException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageService imageService;
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
                username.setCreateAt(LocalDateTime.now());
                /* Image tmpImg = imageService.findById(936993986790369L);
                username.setImage(tmpImg); */
            }
            userRepository.save(username); // lưu user vào database
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
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
        return user != null ? true : false;
    }

    // Tim user bang id
    @Override
    public User findById(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new CustomException(ErrorCode.USER_NOT_FOUND);
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
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
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
        }catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public void setIsOffline(User user) {
        try {
            user.setIsActive(false);
            userRepository.save(user);
            socketWeb.setActive(user);
        }catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @PostConstruct
    public void setActiveUserToFalse() {
        try {
            if (userRepository.count() > 0) {
                userRepository.updateActiveUserToFalse();
            }
        }catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
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
        }catch (QueryTimeoutException e) {
            throw new CustomException(ErrorCode.QUERY_TIMEOUT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public List<User> searchUser(String username) {
        try {
            return userRepository.searchUser(username);
        }catch (QueryTimeoutException e) {
            throw new CustomException(ErrorCode.QUERY_TIMEOUT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public User findByUserWithUserAbouts(Long id) {
        try {
            return userRepository.findByUserWithUserAbouts(id);
        }catch (QueryTimeoutException e) {
            throw new CustomException(ErrorCode.QUERY_TIMEOUT);
        }  catch (NoResultException e) {
            throw new CustomException(ErrorCode.EMPTY_RESULT);
        } catch (NonUniqueResultException e) {
            throw new CustomException(ErrorCode.NON_UNIQUE_RESULT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }
}
