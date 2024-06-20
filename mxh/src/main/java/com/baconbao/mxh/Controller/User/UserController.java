package com.baconbao.mxh.Controller.User;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import com.baconbao.mxh.DTO.ImageDTO;
import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Models.VerifycationToken;
import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.CloudinaryService;
import com.baconbao.mxh.Services.Service.MailService;
import com.baconbao.mxh.Services.Service.VerifycationTokenService;
import com.baconbao.mxh.Services.Service.Post.ImageService;
import com.baconbao.mxh.Services.Service.Post.PostService;
import com.baconbao.mxh.Services.Service.Post.StatusService;
import com.baconbao.mxh.Services.Service.User.RelationshipService;
import com.baconbao.mxh.Services.Service.User.UserService;

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
    @Autowired
    private ImageService imageService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private RelationshipService relationshipService;

    // Nhan trang edit dieu kien la "/editaccount"
    @GetMapping("/editaccount")
    // model la phan minh tra ve trang html
    // request param la thong tin minh lay duoc sau dau ? cua url
    public String showEditAccountPage(Model model, Principal principal) {
        // tim user theo id
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findByEmail(userDetails.getUsername());
        // chuyen user ve userDTO
        UserDTO userDTO = userService.getUserDTO(user);
        // tra userdto ve html
        model.addAttribute("userDTO", userDTO); // userDTO la ten bien de html lay du lieu, userDTO la bien chua du lieu
        // ten file html
        return "editaccount";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
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
            // rejectValue la phuong thuc tra ve loi cho truong do
            result.rejectValue("email", null, "Email already exists"); // email la ten cua truong, null la ten cua loi,
                                                                       // Email already exists la noi dung loi
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

    @PostMapping("/editaccount")
    // path varriablr la thong tin duoc lay sau dau / cua url
    public String editAccount(Principal principal, Model model, UserDTO userDTO, BindingResult result) {
        // k@gmail.com 1
        // user id=1
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());//lấy ra cái email
        User user = userService.findByEmail(userDetails.getUsername());
        //user tai khoan dang xet truoc thay doi email: kn26066. userDTO: th:field : kn26066.
        if (user.getEmail().equals(userDTO.getEmail())) {
            // chuyen userDTO ve user
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            // luu lai user
            userService.saveUser(user);
            // quay ve trang chu
            return "redirect:/";
        }else{
            if(userService.isEmailExist(userDTO.getEmail())){
                result.rejectValue("email", null, "Email already exists"); // email la ten cua truong, null la ten cua loi,
                                                                               // Email already exists la noi dung loi
                return "editaccount";
            }else{
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                userService.saveUser(user);
                return "redirect:/";
            }
        }
    }

    // Duong dan xac nhan
    @GetMapping("/confirmUser")
    public String confirmUser(@RequestParam long token) {
        VerifycationToken verifycationToken = verifycationTokenService.findById(token);
        // neu token het han thi khi an vo chuyen ve register
        if (verifycationToken == null)
            return "register";
        // else xac nhan token va chuyen ve index
        verifycationTokenService.confirmUser(token);
        return "login";
    }

    @GetMapping("/upload")
    public String upload(Model model) {
        ImageDTO imageDTO = new ImageDTO();
        model.addAttribute("imageDTO", imageDTO);
        return "insertImage";
    }

    @PostMapping("/upload")
    public String uploadImage(@ModelAttribute("imageDTO") ImageDTO imageDTO) throws Exception {
        Image image = new Image();
        Map result = cloudinaryService.upload(imageDTO.getFile());
        String imageUrl = (String) result.get("url");
        image.setUrlImage(imageUrl);
        imageService.saveImage(image);
        return "index";
    }

    // Lay duong dan anh de tai ve
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("public_id") String publicId) throws IOException {
        // Lay url tren duong dan anh de tai ve
        String imageUrl = cloudinaryService.getImageUrl(publicId);
        // doi duong link thanh bit
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
        //
        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        //
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg");
        headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(imageBytes.length)
                .body(resource);
    }

    @GetMapping("/adduser")
    public String addUser(Model model) {
        User userOne = userService.findById(1);
        User userTwo = userService.findById(2);
        relationshipService.addUser(userOne, userTwo);
        return "index";
    }
    
}
