package com.baconbao.mxh.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Relationship;

public interface RelationshipRepository extends JpaRepository<Relationship, String>{
    
}
