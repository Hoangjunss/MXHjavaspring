package com.baconbao.mxh.Services.ServiceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.test;
import com.baconbao.mxh.Repository.TestRepository;
import com.baconbao.mxh.Services.Service.TestService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestServiceImpl implements TestService {
    @Autowired
    private TestRepository testRepository;

    @Override
    public List<test> findByLastNameOrFirstName(String firstName, String lastName) {
        log.info("Search by last name: {}, first name: {}", lastName, firstName);
        return testRepository.findByLastNameOrFirstName(lastName, firstName);
    }

}
