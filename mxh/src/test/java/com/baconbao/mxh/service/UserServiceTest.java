package com.baconbao.mxh.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Repository.User.UserRepository;
import com.baconbao.mxh.Services.Service.User.UserService;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserService userService;


    @Test
    public void testFindByEmail_UserFound() {
        String email = "test1223@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);
        User foundUser = userService.findByEmail(email);
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(email, foundUser.getEmail());
    }

    @Test
    public void testSaveUser(){
        User user = new User(null, null, null, "123456789K", "ABC@gmail.com", null, null, null, null, null, null, null, null, null, null, null, null);
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testIsEmailExist() throws Exception {
        String email = "test1223@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);
        boolean isExist = userService.isEmailExist(email);
        Assertions.assertTrue(isExist);
    }

    @Test
    public void testFindById() throws Exception{
        User user = new User();
        when(userRepository.findById(10L)).thenReturn(java.util.Optional.of(user));
        User foundUser = userService.findById(10L);
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(10L, foundUser.getId());
    }

    @Test
    public void testfillAll() throws Exception{
        List<User> users= new ArrayList<>();
        when(userService.fillAll()).thenReturn(users);
        Assertions.assertNotNull(users);
    }

    @Test
    public void testFindAllByFirstNameOrLastName() throws Exception{
        String name = "John";
        List<User> users= new ArrayList<>();
        when(userService.findAllByFirstNameOrLastName(name)).thenReturn(users);
        Assertions.assertNotNull(users);
        Assertions.assertEquals(name, users.get(0).getFirstName());
    }

}
