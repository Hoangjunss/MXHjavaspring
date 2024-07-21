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

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationReponsitory notificationRepository;
    @Autowired
    private SocketWeb socketWeb;

    @Override
    public Notification findById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if (notification.isPresent()) {
            return notification.get();
        }else{
            throw new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
    }

    @Override
    public List<Notification> findByUser(User user) {
        try {
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
            if (notification.getId()==null) {
                notification.setId(getGenerationId());
                notificationRepository.save(notification);
                socketWeb.sendFriendRequestNotification(notification);
            }else{
                notificationRepository.save(notification);
            }
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_UNABLE_TO_SAVE);
        } catch (DataAccessException e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_ERROR);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() &0x1FFFFFFFFFFFFFL;
    }

    public void createNotification(User user, User userSend, String message, String url) {
        if(user.getId() == userSend.getId()){
            return;
        }
        Notification notification = new Notification(null, message, user, userSend, LocalDateTime.now(), false, url);
        saveNotification(notification);
    }

    @Override
    public int countUncheckedNotifications(User user) {
        return notificationRepository.countUncheckedNotification(user);
    }

    @Override
    public void markAllNotificationAsRead(User user) {
        notificationRepository.markAllNotificationAsRead(user);
    }


}
