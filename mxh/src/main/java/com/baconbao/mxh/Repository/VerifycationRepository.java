package com.baconbao.mxh.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baconbao.mxh.Models.Mail;
import com.baconbao.mxh.Models.VerifycationToken;

public interface VerifycationRepository extends JpaRepository<VerifycationToken, Long>{
        
}
