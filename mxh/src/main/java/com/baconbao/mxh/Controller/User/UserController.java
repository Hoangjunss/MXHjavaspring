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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;

import com.baconbao.mxh.DTO.ApiResponse;
import com.baconbao.mxh.DTO.UserAboutDTO;
import com.baconbao.mxh.DTO.UserAboutForm;
import com.baconbao.mxh.DTO.UserDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.VerifycationToken;
import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Models.User.About;
import com.baconbao.mxh.Models.User.Notification;
import com.baconbao.mxh.Models.User.Relationship;
import com.baconbao.mxh.Models.User.StatusRelationship;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Models.User.UserAbout;
import com.baconbao.mxh.Services.CloudinaryService;
import com.baconbao.mxh.Services.Service.VerifycationTokenService;
import com.baconbao.mxh.Services.Service.Post.ImageService;
import com.baconbao.mxh.Services.Service.User.AboutService;
import com.baconbao.mxh.Services.Service.User.NotificationService;
import com.baconbao.mxh.Services.Service.User.RelationshipService;
import com.baconbao.mxh.Services.Service.User.StatusRelationshipService;
import com.baconbao.mxh.Services.Service.User.UserAboutService;
import com.baconbao.mxh.Services.Service.User.UserService;

@Controller
@RequestMapping("/")
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
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RelationshipService relationshipService;

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

    @GetMapping("/usercurrent")
    public ResponseEntity<?> userCurrent(Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái email
            User user = userService.findByEmail(userDetails.getUsername());
            return ResponseEntity.ok(user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("userDTO") UserDTO userDTO) {
        try {
            if (userService.isEmailExist(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Email already exists"));
            }
            LocalDateTime localDateTime = LocalDateTime.now(); // Lấy thời gian hiện tại theo máy

            // Chuyển đổi LocalDateTime sang Date
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            Date date = Date.from(zonedDateTime.toInstant());

            userDTO.setCreateAt(date);
            User user = userService.getUser(userDTO);
            verifycationTokenService.registerUser(user); // gửi mail xác nhận
            return ResponseEntity.ok(new ApiResponse(true, "Register successfull"));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    // SỬA IF
    /*
     * @PostMapping("/editaccount")
     * // path varriablr la thong tin duoc lay sau dau / cua url
     * public String editAccount(Principal principal, Model model, UserDTO userDTO,
     * BindingResult result) {
     * // k@gmail.com 1
     * // user id=1
     * UserDetails userDetails =
     * userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái
     * email
     * User user = userService.findByEmail(userDetails.getUsername());
     * // user tai khoan dang xet truoc thay doi email: kn26066. userDTO: th:field :
     * // kn26066.
     * if (user.getEmail().equals(userDTO.getEmail())) {
     * // chuyen userDTO ve user
     * user.setFirstName(userDTO.getFirstName());
     * user.setLastName(userDTO.getLastName());
     * // luu lai user
     * userService.saveUser(user);
     * // quay ve trang chu
     * return "redirect:/";
     * } else {
     * if (userService.isEmailExist(userDTO.getEmail())) {
     * result.rejectValue("email", null, "Email already exists"); // email la ten
     * cua truong, null la ten cua
     * // loi, Email already exists la noi dung loi
     * return "editaccount";
     * } else {
     * user.setFirstName(userDTO.getFirstName());
     * user.setLastName(userDTO.getLastName());
     * user.setEmail(userDTO.getEmail());
     * userService.saveUser(user);
     * return "redirect:/";
     * }
     * }
     * }
     */
    @PostMapping("/editaccount")
    public ResponseEntity<?> editaccount(@RequestParam("userDTO") UserDTO userDTO, Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái email
            User user = userService.findByEmail(userDetails.getUsername());
            if (user.getEmail().equals(userDTO.getEmail())) {
                // chuyen userDTO ve user
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                // luu lai user
                userService.saveUser(user);
                // quay ve trang chu
                return ResponseEntity.ok(new ApiResponse(true, "Edit account successfull"));
            }
            if (userService.isEmailExist(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Email already exists"));
            }
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            userService.saveUser(user);
            return ResponseEntity.ok(new ApiResponse(true, "Edit account successfull"));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
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

    // Lay danh sach ban be. SỬA DÒNG FOR

    @PostMapping("/relationship")
    public ResponseEntity<?> relationship(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Long userId = Long.parseLong(payload.get("userId").toString());
            Long status = Long.parseLong(payload.get("status").toString());
            System.out.println(userId + " " + status + " /RELATIONSHIP");
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User userOne = userService.findByEmail(userDetails.getUsername());
            User userTwo = userService.findById(userId);
            Relationship relationship = relationalService.findRelationship(userOne, userTwo);
            if (relationship == null || (relationship != null && relationship.getStatus().getId() == 4)) {
                relationship.setUserOne(userOne);
                relationship.setUserTwo(userTwo);
                relationship.setStatus(statusRelationshipService.findById(status));
                relationalService.addUser(relationship);
            } else {
                relationship.setStatus(statusRelationshipService.findById(status));
                relationalService.addUser(relationship);
            }

            boolean success = true;// result of the update logic
            long newStatus = status; // the new status after update

            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("newStatus", newStatus);
            Notification notification = new Notification();
            notification.setMessage(
                    "You have a friend request from " + userOne.getFirstName() + " " + userOne.getLastName());
            notification.setUser(userTwo);
            notification.setChecked(false);
            notification.setUrl("/friends");
            notificationService.saveNotification(notification);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // dat trang thai cua 2 user
    /*
     * @PostMapping("/setfriend")
     * public String setFriend(Principal principal, @RequestParam long id) {
     * // tim user dang thao tac
     * UserDetails userDetails =
     * userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái
     * email
     * User user = userService.findByEmail(userDetails.getUsername());
     * // lay user bang id duoc post len
     * User friend = userService.findById(id);
     * 
     * // tim kiem moi quan he giua 2 user
     * Relationship relationshipUser = relationalService.findRelationship(user,
     * friend);
     * StatusRelationship status = new StatusRelationship();
     * // neu giua 2 user khong co moi quan he
     * if (relationshipUser.getStatus() == null) {
     * // lay status co moi quan he la 1 de gan cho user
     * status = statusRelationshipService.findById(1L);
     * relationshipUser = new Relationship();
     * } else { // neu giua 2 user co quan he truoc do
     * // lay trang thai hien tai cua 2 user la gi
     * status = relationshipUser.getStatus();
     * // do la chi co 4 trang thai nen se set lai trang thai ban dau
     * if (status.getId() == 4) {
     * status = statusRelationshipService.findById(1L);
     * } else {
     * // neu trang thai tu 1 den 3 thi nang len mot bac
     * status = statusRelationshipService.findById(status.getId() + 1);
     * }
     * }
     * // luu moi quan he
     * relationshipUser.setStatus(status);
     * relationshipUser.setUserOne(user);
     * relationshipUser.setUserTwo(friend);
     * relationalService.addUser(relationshipUser);
     * Notification notification = new Notification();
     * notification.setMessage("You have a friend request from " +
     * user.getFirstName() + " " + user.getLastName());
     * notification.setUser(friend);
     * notification.setChecked(false);
     * notification.setUrl("/friends");
     * notificationService.saveNotification(notification);
     * return "redirect:/";
     * }
     */

    @PostMapping("/setfriend")
    public ResponseEntity<?> setfriend(Principal principal, @RequestParam long id) {
        try {
            // tim user dang thao tac
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái email
            User user = userService.findByEmail(userDetails.getUsername());
            // lay user bang id duoc post len
            User friend = userService.findById(id);
            Relationship relationshipUser = relationalService.findRelationship(user, friend);
            StatusRelationship status = new StatusRelationship();
            if (relationshipUser == null) {
                // lay status co moi quan he la 1 de gan cho user
                status = statusRelationshipService.findById(1L);
                relationshipUser = new Relationship();
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
            relationshipUser.setStatus(status);
            relationshipUser.setUserOne(user);
            relationshipUser.setUserTwo(friend);
            relationalService.addUser(relationshipUser);
            Notification notification = new Notification();
            notification.setMessage("You have a friend request from " + user.getFirstName() + " " + user.getLastName());
            notification.setUser(friend);
            notification.setChecked(false);
            notification.setUrl("/friends");
            notificationService.saveNotification(notification);

            return ResponseEntity.ok(new ApiResponse(true, "setfriend successfull"));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    //
    @MessageMapping("/friend.add")
    @SendTo("/queue/addfriend")
    public Notification notificationAddFriend(@Payload Map<String, String> idUserMap, Principal principal) {
        String idUser = idUserMap.get("idUser");
        Long userId = Long.valueOf(idUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findByEmail(userDetails.getUsername());
        User userTwo = userService.findById(userId);

        Relationship relationshipUser = relationalService.findRelationship(user, userTwo);
        StatusRelationship status = new StatusRelationship();

        if (relationshipUser.getStatus() == null) {
            status = statusRelationshipService.findById(1L);
            relationshipUser = new Relationship();
        } else {
            status = relationshipUser.getStatus();
            if (status.getId() == 4) {
                status = statusRelationshipService.findById(1L);
            } else {
                status = statusRelationshipService.findById(status.getId() + 1);
            }
        }
        relationshipUser.setStatus(status);
        relationshipUser.setUserOne(user);
        relationshipUser.setUserTwo(userTwo);
        relationalService.addUser(relationshipUser);
        Notification notification = new Notification();
        notification.setMessage("You have a friend request from " + user.getFirstName() + " " + user.getLastName());
        notification.setUser(userTwo);
        notification.setChecked(false);
        notification.setUrl("/friends");
        notificationService.saveNotification(notification);
        return notification;
    }

    @GetMapping("/hello")
    public String addFriendTest() {
        return "addfriend";
    }

    /*
     * @PostMapping("/uploaduserimg")
     * public String uploadUserImg(@RequestParam("image") MultipartFile image,
     * Principal principal) {
     * try {
     * UserDetails userDetails =
     * userDetailsService.loadUserByUsername(principal.getName());
     * User user = userService.findByEmail(userDetails.getUsername());
     * 
     * Image img = new Image();
     * Map<String, Object> resultMap = cloudinaryService.upload(image);
     * String imageUrl = (String) resultMap.get("url");
     * img.setUrlImage(imageUrl);
     * imageService.saveImage(img);
     * Image tmpImg = imageService.findByImage(img.getUrlImage());
     * user.setImage(tmpImg);
     * userService.saveUser(user);
     * 
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * return "redirect:/";
     * }
     */
    @PostMapping("/uploaduserimg")
    public ResponseEntity<?> uploaduserimg(@RequestParam("image") MultipartFile image, Principal principal) {
        try {
            // tim user dang thao tac
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

            return ResponseEntity.ok(new ApiResponse(true, "uploaduserimg successfull"));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @GetMapping("/uploaduserimg")
    public String uploadUserImgPage() {
        return "test";
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
            return "redirect:/profile?id="+user.getId();
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @GetMapping("/hellosearch")
    public String searchUserPage() {
        return "searchuser";
    }

    // tìm kiếm bạn bè theo tên và hiển thị ra danh sách bạn bè. SỬA DÒNG FOR
    @PostMapping("/usersearch")
    public String searchUser(@RequestParam("username") String username, RedirectAttributes redirectAttributes,
            Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findByEmail(userDetails.getUsername());
        List<User> users = userService.searchUser(username);
        List<User> friends = new ArrayList<>();
        List<User> notFriends = new ArrayList<>();

        for (User u : users) {
            if (u.getId() == user.getId()) {
                continue; // Bỏ qua user hiện tại
            }
            Relationship relationship = relationalService.findRelationship(user, u);
            if (relationship != null) {
                if (relationship.getStatus().getId() == 4) {
                    notFriends.add(u);
                } else {
                    friends.add(u);
                }
            } else {
                notFriends.add(u);
            }
        }
        redirectAttributes.addFlashAttribute("friends", friends);
        redirectAttributes.addFlashAttribute("notFriends", notFriends);
        System.out.println("Not Friends : ");
        return "redirect:/search";
    }

    @GetMapping("/search")
    public String showSearchResults(Model model) {
        // Model sẽ chứa các thuộc tính từ RedirectAttributes trong phương thức POST
        return "searchuser";
    }

    //Lấy danh sách thông báo
    @GetMapping("/api/notifications")
    public ResponseEntity<?> getNotifications(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findByEmail(userDetails.getUsername());
        List<Notification> notifications = notificationService.findByUser(user);
        response.put("notifications", notifications);
        return ResponseEntity.ok(response);
    }

    // Chấp nhận yêu cầu kết bạn
    @PostMapping("/acceptFriendRequest")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Long userId = Long.parseLong(payload.get("userId").toString());
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User userOne = userService.findByEmail(userDetails.getUsername()); // user dang dang nhap
            User userTwo = userService.findById(userId); // user duoc chon
            Relationship relationship = relationalService.findRelationship(userOne, userTwo);
            relationship.setStatus(statusRelationshipService.findById(2L));
            relationalService.addUser(relationship);
            boolean success = true;
            Map<String, Object> response = new HashMap<>();
            response.put("relationship", relationship);
            response.put("success", success);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Xóa bạn
    @PostMapping("/deleteFriend")
    public ResponseEntity<?> deleteFriend(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Long userId = Long.parseLong(payload.get("userId").toString());
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User userOne = userService.findByEmail(userDetails.getUsername());
            User userTwo = userService.findById(userId);
            Relationship relationship = relationalService.findRelationship(userOne, userTwo);
            relationship.setStatus(statusRelationshipService.findById(4L));
            relationalService.addUser(relationship);
            boolean success = true;
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Kết bạn
    @PostMapping("/addFriend")
    public ResponseEntity<?> addFriend(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Long userId = Long.parseLong(payload.get("userId").toString());
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User userOne = userService.findByEmail(userDetails.getUsername());
            User userTwo = userService.findById(userId);
            Relationship relationship = relationalService.findRelationship(userOne, userTwo);
            if (relationship == null || (relationship != null && relationship.getStatus().getId() == 4)) {
                relationship.setUserOne(userOne);
                relationship.setUserTwo(userTwo);
                relationship.setStatus(statusRelationshipService.findById(1L));
                relationalService.addUser(relationship);
            }
            boolean success = true;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //?
    @GetMapping("/editaccount")
    public ResponseEntity<?> editaccount(Principal principal) {
        try {
            // tim user theo id
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            // chuyen user ve userDTO
            UserDTO userDTO = userService.getUserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @GetMapping("/listfriend")
    public String getMethodName() {
        return "seefriend";
    }
    

    // Lấy danh sách bạn bè
    @GetMapping("/friends")
    public ResponseEntity<?> friends(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            // tim user theo id
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            List<Relationship> relationships = relationshipService.findRelationshipPending(user);
            response.put("relationships", relationships);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    //Lấy danh sách không là bạn bè
    @GetMapping("/notFriend")
    public ResponseEntity<?> notfriends(Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            List<User> notFriends = relationalService.findNotFriends(user);
            return ResponseEntity.ok(notFriends);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    //Đếm số lượng bạn bè - LƯU Ý STATUS
    @GetMapping("/mutualFriend/{friendId}")
    public ResponseEntity<?> countFriend(@PathVariable Long friendId, Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            int count = relationalService.countMutualFriends(user, friendId);
            System.out.println("Count: " + count);
            Map<String, Integer> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    //Lấy phần About (người dùng mô tả thêm về bản thân)
    @GetMapping("/api/getabouts")
    public ResponseEntity<?> editprofile(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            // tim user theo id
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            List<About> abouts = aboutService.fillAll();
            // Lấy danh sách mô tả trước đó của người dùng
            List<UserAbout> userAbouts = userAboutService.findByUser(user);
            response.put("abouts", abouts);
            response.put("userAbouts", userAbouts);
            // Tạo một map để dễ dàng tra cứu mô tả theo idAbout
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    //Lấy trang User/profile.html
    @GetMapping("/countfriend")
    public ResponseEntity<?> countFriend(Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User loggedInUser = userService.findByEmail(userDetails.getUsername());
            int countFriend = relationshipService.countfriend(loggedInUser, statusRelationshipService.findById(1L));
            return ResponseEntity.ok(countFriend);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //Lấy số lượng thông báo chưa xem
    @GetMapping("/countNotificationsIsCheck")
    public ResponseEntity<Map<String, Object>> getNotificationsIsCheck(Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User loggedInUser = userService.findByEmail(userDetails.getUsername());
        int unreadCount = notificationService.countUncheckedNotifications(loggedInUser);
        Map<String, Object> response = new HashMap<>();
        response.put("unreadCount", unreadCount);
        return ResponseEntity.ok(response);
    }

    // Lấy user
    @GetMapping("/api/getuser")
    public ResponseEntity<?> getUser(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findById(id);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    // Lấy mối quan hệ - tín hiệu có phải user logged - user đối diện
    @GetMapping("/api/getrelationship")
    public ResponseEntity<?> getRelationship(@RequestParam Long userId, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User loggedInUser = userService.findByEmail(userDetails.getUsername());
        User user = userService.findById(userId);
        Relationship relationship = relationalService.findRelationship(loggedInUser, user);
        boolean isOwnUser = loggedInUser.getId() == user.getId() ? true : false;
        Map<String, Object> response = new HashMap<>();
        response.put("isOwnUser", isOwnUser);
        response.put("user", user);
        response.put("relationship", relationship);
        return ResponseEntity.ok(response);
    }
}
