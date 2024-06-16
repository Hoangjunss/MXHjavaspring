package com.baconbao.mxh.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.User;
import java.util.List;


@Repository

public interface UserRepository extends JpaRepository<User,Long> {
    User  findByEmail(String email);
}
