package com.baconbao.mxh.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.Post;
import com.baconbao.mxh.Models.Status;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStatus(Status status);
}
