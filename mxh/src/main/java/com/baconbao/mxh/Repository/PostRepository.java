package com.baconbao.mxh.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.Post;
import com.baconbao.mxh.Models.Status;

import jakarta.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStatus(Status status);
}
