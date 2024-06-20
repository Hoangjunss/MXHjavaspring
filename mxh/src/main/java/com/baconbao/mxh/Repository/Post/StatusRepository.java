package com.baconbao.mxh.Repository.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Post.Status;

public interface StatusRepository extends JpaRepository<Status,Long> {
    
}
