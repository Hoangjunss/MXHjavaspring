package com.baconbao.mxh.Controller.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

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
import com.baconbao.mxh.Models.Post;
import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Models.VerifycationToken;
import com.baconbao.mxh.Services.Service.MailService;
import com.baconbao.mxh.Services.Service.PostService;
import com.baconbao.mxh.Services.Service.UserService;
import com.baconbao.mxh.Services.Service.VerifycationTokenService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private VerifycationTokenService verifycationTokenService;
    @Autowired
    private PostService postService;

    // Nhan trang chu dieu kien la "/"
    @GetMapping({ "/", "" })
    public String showIndexPage() {
        return "index";
    }

    // Nhan trang edit dieu kien la "/editaccount"
    @GetMapping("/editaccount")
    // model la phan minh trar ve trang html
    // request param la thong tin minh lay duoc sau dau ? cua url
    public String showEditAccountPage(Model model, @RequestParam long id) {
        // tim user theo id
        User user = userService.findById(id);
        // chuyen user ve userDTO
        UserDTO userDTO = userService.getUserDTO(user);
        // tra userdto ve html
        model.addAttribute("userDTO", userDTO);
        // ten file html
        return "editaccount";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    @GetMapping("/uploadpost")
    public String showUploadPostPage(Model model) {
        Post post = new Post();
        return "uploadpost";
    }

    // Nhan duong dan va trang ve trang register.html trong templates
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("userDTO", userDTO);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model) {
        // kiem tra email da ton tai hay chua
        if (userService.isEmailExist(userDTO.getEmail())) {
            // neu ton tai thi tra ve trang register va thong bao loi
            result.rejectValue("email", null, "Email already exists");
            return "register";
        }
        // nếu email chưa tồn tại thì thêm user mới và thêm vào createUser thời gian
        // hiện tại
        // Lấy thời gian hiện tại theo UTC
        /*
         * Instant nowUtc = Instant.now();
         * Timestamp timestampUtc = Timestamp.from(nowUtc);
         * userDTO.setCreateAt(timestampUtc);
         * userService.saveUser(userService.getUser(userDTO));
         */
        LocalDateTime localDateTime = LocalDateTime.now();

        // Chuyển đổi LocalDateTime sang Date
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());

        userDTO.setCreateAt(date);
        User user = userService.getUser(userDTO);
        verifycationTokenService.registerUser(user);
        // quay về trang login
        return "redirect:/login";
    }

    @PostMapping("/editaccount/{id}")
    // path varriablr la thong tin duoc lay sau dau / cua url
    public String editAccount(@PathVariable Long id, Model model, UserDTO userDTO, BindingResult result) {
        User user = userService.findById(id);
        // kiem tra email da ton tai hay chua
        if (userService.isEmailExist(userDTO.getEmail())) {
            // neu ton tai thi tra ve trang editaccount va thong bao loi
            result.rejectValue("email", null, "Email already exists");
            return "editaccount";
        }
        // chuyen userDTO ve user
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        // luu lai user
        userService.saveUser(user);
        // quay ve trang chu
        return "redirect:/";
    }

    //Duong dan xac nhan 
    @GetMapping("/confirmUser")
    public String confirmUser(@RequestParam long token) {
        VerifycationToken verifycationToken = verifycationTokenService.findById(token);
        //neu token het han thi khi an vo chuyen ve register
        if(verifycationToken == null) return "register";
        //else xac nhan token va chuyen ve index
        verifycationTokenService.confirmUser(token);
        return "index";
    }

    @PostMapping("/uploadpost")
    public String uploadPost(@ModelAttribute("post") Post post) {
        LocalDateTime expiryTime = LocalDateTime.now();
        post.setCreateAt(expiryTime);
        post.setUpdateAt(expiryTime);
        postService.save(post);
        return "redirect:/index";
    }


}
