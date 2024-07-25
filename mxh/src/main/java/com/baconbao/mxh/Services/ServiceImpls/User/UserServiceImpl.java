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
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.UserRepository;
import com.baconbao.mxh.Services.Service.User.UserService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.QueryTimeoutException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    @Override
    public void saveUser(User username) {
        try {
            log.info("Save user {}", username.getEmail());
            if (username.getId() == null) {
                username.setId(getGenerationId());
                username.setPassword(passwordEncoder.encode(username.getPassword())); // mã hóa mật khẩu
                username.setCreateAt(LocalDateTime.now());
            }
            userRepository.save(username); // lưu user vào database
        } catch (DataIntegrityViolationException e) {
            log.error("User unable to save with Email {}", username.getEmail());
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public User findByEmail(String email) {
        log.info("Finding user for email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found for email: {}", email);
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } else {
            return userRepository.findByEmail(email); // tìm kiếm user theo email
        }
    }

    @Override
    public boolean isEmailExist(String email) {
        log.info("Check email is exist: {}", email);
        User user = userRepository.findByEmail(email); // tìm kiếm user theo email
        return user != null ? true : false;
    }

    @Override
    public User findById(long userId) {
        log.info("Finding user for id: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    @Override
    public List<User> fillAll() {
        log.info("Finding all users");
        try {
            return userRepository.findAll();
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    public boolean isEmailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public void setIsOnline(User user) {
        try {
            log.info("Set user {} isOnline", user.getEmail());
            user.setIsActive(true);
            userRepository.save(user);
            socketWeb.setActive(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Error setting user is online with email {}", user.getEmail());
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public void setIsOffline(User user) {
        try {
            log.info("Set user {} is offline", user.getEmail());
            user.setIsActive(false);
            userRepository.save(user);
            socketWeb.setActive(user);
        } catch (DataIntegrityViolationException e) {
            log.error("Error setting user is offline with email {}", user.getEmail());
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @PostConstruct
    public void setActiveUserToFalse() {
        try {
            log.info("Set is active user to false");
            if (userRepository.count() > 0) {
                userRepository.updateActiveUserToFalse();
            }
        } catch (DataIntegrityViolationException e) {
            log.error("Error setting is active user to false");
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    // Lỗi truy vấn like
    @Override
    public List<User> findAllByFirstNameOrLastName(String name) {
        try {
            log.info("Finding by name user: {}", name);
            return userRepository.findByLastNameOrFirstName(name, name);
        } catch (QueryTimeoutException e) {
            throw new CustomException(ErrorCode.QUERY_TIMEOUT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public List<User> searchUser(String username) {
        try {
            log.info("Searching user by username: {}", username);
            return userRepository.searchUser(username);
        } catch (QueryTimeoutException e) {
            throw new CustomException(ErrorCode.QUERY_TIMEOUT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    @Override
    public User findByUserWithUserAbouts(Long id) {
        try {
            log.info("Finding user by id with user abouts: {}", id);
            return userRepository.findByUserWithUserAbouts(id);
        } catch (QueryTimeoutException e) {
            throw new CustomException(ErrorCode.QUERY_TIMEOUT);
        } catch (NoResultException e) {
            throw new CustomException(ErrorCode.EMPTY_RESULT);
        } catch (NonUniqueResultException e) {
            throw new CustomException(ErrorCode.NON_UNIQUE_RESULT);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }
}
