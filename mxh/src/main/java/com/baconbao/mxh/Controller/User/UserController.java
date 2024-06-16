package com.baconbao.mxh.Controller.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Services.Service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/editaccount")
    public String showEditAccountPage(Model model, @RequestParam long id) {
        User user = userService.findById(id);
        UserDTO userDTO = userService.getUserDTO(user);
        model.addAttribute("userDTO", userDTO);
        return "editaccount";
    }

    @PostMapping("/editaccount/{id}")
    public String editAccount(@PathVariable Long id, Model model, UserDTO userDTO, BindingResult result) {
        User user = userService.findById(id);
        if (userService.isEmailExist(userDTO.getEmail())) {
            result.rejectValue("email", null, "Email already exists");
            return "editaccount";
        }
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        userService.saveUser(user);
        return "redirect:/";
    }
}
