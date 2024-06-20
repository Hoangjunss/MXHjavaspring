package com.baconbao.mxh.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.baconbao.mxh.Models.VerifycationToken;

import jakarta.transaction.Transactional;

public interface VerifycationRepository extends JpaRepository<VerifycationToken, Long>{
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM verifycation_token WHERE set_expiry_date < :date", nativeQuery = true) // truy van tat ca token co thoi gian be hon 
    List<VerifycationToken> findExpiredVerificationTokens(@Param ("date") LocalDateTime date); // tim kiem token het han 
}
