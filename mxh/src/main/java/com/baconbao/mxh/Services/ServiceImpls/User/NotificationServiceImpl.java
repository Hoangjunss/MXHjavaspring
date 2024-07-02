package com.baconbao.mxh.Services.ServiceImpls.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Config.Socket.SocketWeb;
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
            throw new RuntimeException("Notification not found");
        }
    }

    @Override
    public List<Notification> findByUser(User user) {
        return notificationRepository.findByUser(user);
    }

    @Override
    public List<Notification> findByUserAndChecked(User user, boolean isChecked) {
        return notificationRepository.findByUserAndIsChecked(user, isChecked);
    }

    @Override
    public void saveNotification(Notification notification) {
        if (notification.getId()==null) {
            notification.setId(getGenerationId());
            notificationRepository.save(notification);
            socketWeb.sendFriendRequestNotification(notification);
        }else{
            notificationRepository.save(notification);
        }
    }

    public Long getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return uuid.getMostSignificantBits() & Long.MAX_VALUE;
    }

    @Override
    public int countUncheckedNotifications(User user) {
        return notificationRepository.countUncheckedNotification(user);
    }
}
