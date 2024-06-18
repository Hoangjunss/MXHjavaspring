package com.baconbao.mxh.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Image;

public interface ImageRepository extends JpaRepository<Image,Long> {
    
}
