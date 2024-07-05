package com.baconbao.mxh.Repository.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Post.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    
}
