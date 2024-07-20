package com.baconbao.mxh.Repository.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Post.Interaction;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.User.User;

public interface InteractionRepository extends JpaRepository<Interaction, Long>{
    Interaction findByPostAndUser(Post post, User user);
}
