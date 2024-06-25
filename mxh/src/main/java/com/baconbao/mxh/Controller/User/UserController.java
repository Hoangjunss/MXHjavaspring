package com.baconbao.mxh.Controller.User;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;

import com.baconbao.mxh.DTO.UserAboutDTO;
import com.baconbao.mxh.DTO.UserAboutForm;
import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.VerifycationToken;
import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Models.User.About;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.StatusRelationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Models.User.UserAbout;
import com.baconbao.mxh.Services.CloudinaryService;
import com.baconbao.mxh.Services.Service.VerifycationTokenService;
import com.baconbao.mxh.Services.Service.Post.ImageService;
import com.baconbao.mxh.Services.Service.User.AboutService;
import com.baconbao.mxh.Services.Service.User.RelationshipService;
import com.baconbao.mxh.Services.Service.User.StatusRelationshipService;
import com.baconbao.mxh.Services.Service.User.UserAboutService;
import com.baconbao.mxh.Services.Service.User.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private VerifycationTokenService verifycationTokenService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RelationshipService relationalService;
    @Autowired
    private StatusRelationshipService statusRelationshipService;
    @Autowired
    private AboutService aboutService;
    @Autowired
    private UserAboutService userAboutService;

    // Nhan trang edit dieu kien la "/editaccount"
    @GetMapping("/editaccount")
    // model la phan minh tra ve trang html
    // principal giông như session, chứa thông tin người dùng
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
        return "/User/Login";
    }

    // Nhan duong dan va trang ve trang register.html trong templates
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("userDTO", userDTO);
        return "/User/Register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model) {
        // BindingResult la doi tuong chua loi cua truong, neu co loi thi tra ve trang
        // kiem tra email da ton tai hay chua
        if (userService.isEmailExist(userDTO.getEmail())) {
            // neu ton tai thi tra ve trang register va thong bao loi
            // rejectValue la phuong thuc tra ve loi cho truong do
            result.rejectValue("email", null, "Email already exists"); // email la ten cua truong, null la ten cua loi,
                                                                       // Email already exists la noi dung loi
            return "register";
        }
        LocalDateTime localDateTime = LocalDateTime.now(); // Lấy thời gian hiện tại theo máy

        // Chuyển đổi LocalDateTime sang Date
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());

        userDTO.setCreateAt(date);
        User user = userService.getUser(userDTO);
        verifycationTokenService.registerUser(user); // gửi mail xác nhận
        // quay về trang login
        return "redirect:/login";
    }

    @PostMapping("/editaccount")
    // path varriablr la thong tin duoc lay sau dau / cua url
    public String editAccount(Principal principal, Model model, UserDTO userDTO, BindingResult result) {
        // k@gmail.com 1
        // user id=1
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái email
        User user = userService.findByEmail(userDetails.getUsername());
        // user tai khoan dang xet truoc thay doi email: kn26066. userDTO: th:field :
        // kn26066.
        if (user.getEmail().equals(userDTO.getEmail())) {
            // chuyen userDTO ve user
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            // luu lai user
            userService.saveUser(user);
            // quay ve trang chu
            return "redirect:/";
        } else {
            if (userService.isEmailExist(userDTO.getEmail())) {
                result.rejectValue("email", null, "Email already exists"); // email la ten cua truong, null la ten cua
                                                                           // loi, Email already exists la noi dung loi
                return "editaccount";
            } else {
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
    public String confirmUser(@RequestParam long token) { // @RequestParam lấy giá trị từ url (lấy giá trị của token từ
                                                          // url)
        VerifycationToken verifycationToken = verifycationTokenService.findById(token);
        // neu token het han thi khi an vo chuyen ve register
        if (verifycationToken == null)
            return "User/Register";
        // else xac nhan token va chuyen ve index
        verifycationTokenService.confirmUser(token);
        return "User/Login";
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

    // Lay danh sach ban be
    @GetMapping("/friends")
    public String getMethodNameString(Principal principal, Model model) {
        // tim danh sach cua user dang dang nhap
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái email
        User user = userService.findByEmail(userDetails.getUsername());
        // tim danh sach ban be cua user
        List<Relationship> relationships = relationalService.findAllByUserOne(user);
        // tra ve html danh sach ban be
        model.addAttribute("relationships", relationships);
        return "index";
    }

    // dat trang thai cua 2 user
    @PostMapping("/setfriend")
    public String setFriend(Principal principal, @RequestParam long id) {
        // tim user dang thao tac
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái email
        User user = userService.findByEmail(userDetails.getUsername());
        // lay user bang id duoc post len
        User friend = userService.findById(id);

        // tim kiem moi quan he giua 2 user
        Relationship relationshipUser = relationalService.findRelationship(user, friend);
        StatusRelationship status = new StatusRelationship();
        // neu giua 2 user khong co moi quan he
        if (relationshipUser == null) {
            // lay status co moi quan he la 1 de gan cho user
            status = statusRelationshipService.findById(1L);
        } else { // neu giua 2 user co quan he truoc do
                 // lay trang thai hien tai cua 2 user la gi
            status = relationshipUser.getStatus();
            // do la chi co 4 trang thai nen se set lai trang thai ban dau
            if (status.getId() == 4) {
                status = statusRelationshipService.findById(1L);
            } else {
                // neu trang thai tu 1 den 3 thi nang len mot bac
                status = statusRelationshipService.findById(status.getId() + 1);
            }
        }
        // luu moi quan he
        relationshipUser.setStatus(status);
        relationshipUser.setUserOne(user);
        relationshipUser.setUserTwo(friend);
        relationalService.addUser(relationshipUser);
        return "redirect:/";
    }

    @GetMapping("/hello")
    public String addFriendTest() {
        return "addfriend";
    }

    @PostMapping("/uploaduserimg")
    public String uploadUserImg(@RequestParam("image") MultipartFile image, Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());

            Image img = new Image();
            Map<String, Object> resultMap = cloudinaryService.upload(image);
            String imageUrl = (String) resultMap.get("url");
            img.setUrlImage(imageUrl);
            imageService.saveImage(img);
            Image tmpImg = imageService.findByImage(img.getUrlImage());
            user.setImage(tmpImg);
            userService.saveUser(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/uploaduserimg")
    public String uploadUserImgPage() {
        return "test";
    }

    @GetMapping("/editprofile")
    public String editProfile(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findByEmail(userDetails.getUsername());

        List<About> abouts = aboutService.fillAll();
        UserAboutForm userAboutForm = new UserAboutForm();
        List<UserAboutDTO> userAboutDTOs = new ArrayList<>();

        // Lấy danh sách mô tả trước đó của người dùng
        List<UserAbout> userAbouts = userAboutService.findByUser(user);

        // Tạo một map để dễ dàng tra cứu mô tả theo idAbout
        Map<Long, String> userAboutMap = new HashMap<>();
        for (UserAbout userAbout : userAbouts) {
            userAboutMap.put(userAbout.getAbout().getId(), userAbout.getDescription());
        }
        for (About about : abouts) {
            UserAboutDTO dto = new UserAboutDTO();
            dto.setAboutId(about.getId());

            // Kiểm tra xem có mô tả trước đó hay không, nếu có thì gán vào DTO
            if (userAboutMap.containsKey(about.getId())) {
                dto.setDescription(userAboutMap.get(about.getId()));
            }
            userAboutDTOs.add(dto);
        }

        userAboutForm.setUserAboutDTOs(userAboutDTOs);
        model.addAttribute("abouts", abouts);
        model.addAttribute("userAboutForm", userAboutForm);
        return "profile";
    }

    @PostMapping("/editprofile")
    public String editProfile(@ModelAttribute("userAboutForm") UserAboutForm userAboutForm, Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            for (UserAboutDTO userAboutDTO : userAboutForm.getUserAboutDTOs()) {
                About about = aboutService.findById(userAboutDTO.getAboutId());
                UserAbout userAbout = new UserAbout();
                userAbout.setUser(user);
                userAbout.setAbout(about);
                userAbout.setDescription(userAboutDTO.getDescription());
                userAboutService.save(userAbout);
            }
            return "redirect:/editprofile";
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

}
