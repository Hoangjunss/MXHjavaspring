package com.baconbao.mxh.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baconbao.mxh.Models.User.Notification;
import com.baconbao.mxh.Models.User.User;

import jakarta.transaction.Transactional;

import java.util.List;

public interface NotificationReponsitory extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreateAtDesc(User user);

    List<Notification> findByIsCheckedOrderByCreateAtDesc(boolean isChecked);

    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.isChecked = :isChecked")
    List<Notification> findByUserAndIsChecked(@Param("user") User user, @Param("isChecked") boolean isChecked);

    @Query("SELECT count(n) FROM Notification n WHERE n.user = :user AND n.isChecked = false")
    int countUncheckedNotification(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isChecked = true WHERE n.user = :user")
    void markAllNotificationAsRead(@Param("user") User user);

}
