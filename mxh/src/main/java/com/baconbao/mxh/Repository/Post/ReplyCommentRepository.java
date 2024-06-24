package com.baconbao.mxh.Repository.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Post.ReplyComment;

public interface ReplyCommentRepository  extends JpaRepository<ReplyComment,Long>{
    
}
