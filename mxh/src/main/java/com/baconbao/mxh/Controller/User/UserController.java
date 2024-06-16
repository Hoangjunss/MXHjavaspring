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

    //Nhan trang chu dieu kien la "/"
    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    //Nhan trang edit dieu kien la "/editaccount"
    @GetMapping("/editaccount")
    //model la phan minh trar ve trang html 
    //request param la thong tin minh lay duoc sau dau ? cua url
    public String showEditAccountPage(Model model, @RequestParam long id) {
        //tim user theo id
        User user = userService.findById(id);
        //chuyen user ve userDTO
        UserDTO userDTO = userService.getUserDTO(user);
        //tra userdto ve html
        model.addAttribute("userDTO", userDTO);
        //ten file html
        return "editaccount";
    }

    
    @PostMapping("/editaccount/{id}")
    //path varriablr la thong tin duoc lay sau dau / cua url
    public String editAccount(@PathVariable Long id, Model model, UserDTO userDTO, BindingResult result) {
        User user = userService.findById(id);
        //kiem tra email da ton tai hay chua
        if (userService.isEmailExist(userDTO.getEmail())) {
            //neu ton tai thi tra ve trang editaccount va thong bao loi
            result.rejectValue("email", null, "Email already exists");
            return "editaccount";
        }
        //chuyen userDTO ve user
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        //luu lai user
        userService.saveUser(user);
        //quay ve trang chu
        return "redirect:/";
    }
}
