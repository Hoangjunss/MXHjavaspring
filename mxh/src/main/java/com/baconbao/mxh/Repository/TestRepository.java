package com.baconbao.mxh.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.test;

public interface TestRepository extends JpaRepository<test, Long>{

    List<test> findByLastNameOrFirstName(String lastName, String firstName);

}
