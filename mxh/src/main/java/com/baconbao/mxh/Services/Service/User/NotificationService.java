package com.baconbao.mxh.Services.Service.User;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.User.Notification;
import com.baconbao.mxh.Models.User.User;

@Service
public interface NotificationService {
    Notification findById(Long id);
    void saveNotification(Notification notification);
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndChecked(User user, boolean checked);
    int countUncheckedNotifications(User user);
    void markAllNotificationAsRead(User user);
    void createNotification(User userTwo,User userOne,String message, String url);
}
