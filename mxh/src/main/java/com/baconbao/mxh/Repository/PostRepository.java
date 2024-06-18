package com.baconbao.mxh.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.Post;

@Repository
public interface PostRepository  extends JpaRepository<Post, Long>{
    
}
