package com.baconbao.mxh.Services.ServiceImpls.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Config.Socket.SocketWeb;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.User.Notification;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.NotificationReponsitory;
import com.baconbao.mxh.Services.Service.User.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationReponsitory notificationRepository;
    @Autowired
    private SocketWeb socketWeb;

    @Override
    public Notification findById(Long id) {
        log.info("Find notification by id: {}", id);
        Optional<Notification> notification = notificationRepository.findById(id);
        if (notification.isPresent()) {
            return notification.get();
        } else {
            log.error("Failed to find notifica by id: {} is not found", id);
            throw new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
    }

    @Override
    public List<Notification> findByUser(User user) {
        try {
            log.info("Finding notifica with user by user id: ", user.getId());
            return notificationRepository.findByUserOrderByCreateAtDesc(user);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public List<Notification> findByUserAndChecked(User user, boolean isChecked) {
        try {
            log.info("Finding notifica with user and checked by user id: {}, checked status: {}", user.getId(),
                    isChecked);
            return notificationRepository.findByUserAndIsChecked(user, isChecked);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void saveNotification(Notification notification) {
        try {
            log.info("Saveing notification");
            if (notification.getId() == null) {
                notification.setId(getGenerationId());
                notificationRepository.save(notification);
                socketWeb.sendFriendRequestNotification(notification);
            } else {
                notificationRepository.save(notification);
            }
        } catch (DataIntegrityViolationException e) {
            log.error("failed to save notification");
            throw new CustomException(ErrorCode.NOTIFICATION_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & 0x1FFFFFFFFFFFFFL;
    }

    public void createNotification(User user, User userSend, String message, String url) {
        if (user.getId() == userSend.getId()) {
            return;
        }
        Notification notification = new Notification(null, message, user, userSend, LocalDateTime.now(), false, url);
        saveNotification(notification);
    }

    @Override
    public int countUncheckedNotifications(User user) {
        log.info("Counting unchecked notification for user: {}", user.getId());
        return notificationRepository.countUncheckedNotification(user);
    }

    @Override
    public void markAllNotificationAsRead(User user) {
        log.info("Marking all notification as read for user: {}", user.getId());
        notificationRepository.markAllNotificationAsRead(user);
    }

}
