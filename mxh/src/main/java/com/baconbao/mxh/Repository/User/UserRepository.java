package com.baconbao.mxh.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.baconbao.mxh.Models.User.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);

  /* @Modifying
  @Transactional */
  @Query(value = "SELECT MAX(id_user) FROM user", nativeQuery = true)
  Integer countById();
}
