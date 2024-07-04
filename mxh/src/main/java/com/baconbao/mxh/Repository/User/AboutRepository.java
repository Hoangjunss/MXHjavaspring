package com.baconbao.mxh.Repository.User;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baconbao.mxh.Models.User.About;
import com.baconbao.mxh.Models.User.UserAbout;
import com.baconbao.mxh.Models.User.User;

public interface AboutRepository extends JpaRepository<About,Long> {
    @Query("SELECT ua FROM UserAbout ua WHERE ua.user = :user")
    List<UserAbout> findByUser(@Param("user") User user);
}
