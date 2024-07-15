package com.baconbao.mxh.Controller.Controller.User;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.baconbao.mxh.DTO.UserDTO;

@Controller
public class UserController {

    // GET PAGE
    @GetMapping("/login") // Login
    public String showLoginPage() {
        return "/User/Login";
    }

    @GetMapping("/register") // Register
    public String showRegisterPage(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("userDTO", userDTO);
        return "/User/Register";
    }

    @GetMapping("/hello") // Add friend
    public String addFriendTest() {
        return "addfriend";
    }

    @GetMapping("/uploaduserimg") // Upload Image user
    public String uploadUserImgPage() {
        return "test";
    }

    @GetMapping("/hellosearch") // Search User - test
    public String searchUserPage() {
        return "searchuser";
    }

    @GetMapping("/search") // Search user
    public String showSearchResults() {
        return "searchuser";
    }

    @GetMapping("/listfriend") // List friend
    public String getMethodName() {
        return "seefriend";
    }

    @GetMapping("/profile") // Profile
    public String getProfilePage() {
        return "User/profile";
    }

    @GetMapping("/Confirm") // Confirm
    public String confirm() {
        return "User/Confirm";
    }

}
