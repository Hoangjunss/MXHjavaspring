package com.baconbao.mxh.Repository.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Post.Image;

public interface ImageRepository extends JpaRepository<Image,Long> {
    Image findByUrlImage(String url);
}
