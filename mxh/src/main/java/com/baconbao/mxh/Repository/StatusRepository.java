package com.baconbao.mxh.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Status;

public interface StatusRepository extends JpaRepository<Status,Long> {
    
}
