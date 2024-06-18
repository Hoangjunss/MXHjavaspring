package com.baconbao.mxh.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.Post;

import jakarta.transaction.Transactional;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long>{
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM post WHERE status = :status", nativeQuery = true) // truy van tat ca token co thoi gian be hon 
    List<Post> findByStatus(@Param ("status") String status);
}
