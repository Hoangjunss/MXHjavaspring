package com.baconbao.mxh.Repository.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);

  /*
   * @Modifying
   * 
   * @Transactional
   */
  @Query(value = "SELECT MAX(id_user) FROM user", nativeQuery = true)
  Integer countById();

  @Modifying
  @Transactional
  @Query("UPDATE User u SET u.isActive = false WHERE u.isActive = true")
  void updateActiveUserToFalse();

  @Query(value = "SELECT u.id_user FROM User As u WHERE u.last_name LIKE " + "%:name% OR u.first_name LIKE %:name%", nativeQuery=true)
    List<Long> findAllByFirstNameOrLastName(@Param("name") String name);
}