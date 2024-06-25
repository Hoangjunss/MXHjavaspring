package com.baconbao.mxh.Services.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.test;

@Service
public interface TestService {
    List<test> findByLastNameOrFirstName(String firstName, String lastName);
}
