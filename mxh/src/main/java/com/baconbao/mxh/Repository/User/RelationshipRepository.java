package com.baconbao.mxh.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.User.Relationship;

public interface RelationshipRepository extends JpaRepository<Relationship, Long>{
    
}
