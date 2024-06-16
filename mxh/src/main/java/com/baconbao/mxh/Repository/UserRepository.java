package com.baconbao.mxh.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
