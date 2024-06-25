package com.baconbao.mxh.Services.ServiceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baconbao.mxh.Models.test;
import com.baconbao.mxh.Repository.TestRepository;
import com.baconbao.mxh.Services.Service.TestService;
@Service
public class TestServiceImpl implements TestService{
    @Autowired
    private TestRepository testRepository;

    @Override
    public List<test> findByLastNameOrFirstName(String firstName, String lastName) {
        return testRepository.findByLastNameOrFirstName(lastName, firstName);
    }

}
