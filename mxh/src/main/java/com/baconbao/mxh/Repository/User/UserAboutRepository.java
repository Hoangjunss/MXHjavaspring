package com.baconbao.mxh.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.User.About;

public interface UserAboutRepository extends JpaRepository<About, Long>{

}
