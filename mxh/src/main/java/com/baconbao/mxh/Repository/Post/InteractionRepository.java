package com.baconbao.mxh.Repository.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Post.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction, Long>{
}
