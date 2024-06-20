package com.baconbao.mxh.Repository.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;

public interface RelationshipRepository extends JpaRepository<Relationship, Long>{
    List<Relationship> findAllByUserOneId(Long id);
}
