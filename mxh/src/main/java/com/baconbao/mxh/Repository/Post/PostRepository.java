package com.baconbao.mxh.Repository.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.Post.Status;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByIsActiveAndStatusOrderByCreateAtDesc(boolean active, Status status);
    List<Post> findByIsActiveAndStatus(boolean active, Status status);
}
