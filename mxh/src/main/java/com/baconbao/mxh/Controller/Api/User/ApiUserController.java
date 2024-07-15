package com.baconbao.mxh.Controller.Api.User;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.baconbao.mxh.DTO.ApiResponse;
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

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class ApiUserController {
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

    @GetMapping("/api/confirmUser")
    public String confirmUser(@RequestParam long token, Model model) { // @RequestParam lấy giá trị từ url (lấy giá trị
                                                                       // của token từ url
        try {
            VerifycationToken verifycationToken = verifycationTokenService.findById(token);
            // neu token het han thi khi an vo chuyen ve register
            if (verifycationToken == null)
                return "User/Register";
            // else xac nhan token va chuyen ve index
            verifycationTokenService.confirmUser(token);
            return "User/Login";
        } catch (CustomException e) {
            model.addAttribute("errorConfirmUser", e.getErrorCode().getMessage());
            return "error"; // trả về trang lỗi hoặc một thông báo lỗi
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorConfirmUser", "An unexpected error occurred");
            return "error"; // trả về trang lỗi hoặc một thông báo lỗi
        }
    }

    @GetMapping("/api/usercurrent")
    public ResponseEntity<?> userCurrent(Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());// lấy ra cái email
            User user = userService.findByEmail(userDetails.getUsername());
            Hibernate.initialize(user.getImage());
            return ResponseEntity.ok(user);
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try {
            if (userService.isEmailExist(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Email already exists"));
            }
            userDTO.setCreateAt(LocalDateTime.now());
            User user = userService.getUser(userDTO);
            verifycationTokenService.registerUser(user);
            return ResponseEntity.ok(new ApiResponse(true, "Register successful"));
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/editaccount")
    public ResponseEntity<?> editaccount(@RequestBody UserDTO userDTO, Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            if (user.getEmail().equals(userDTO.getEmail())) {
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                userService.saveUser(user);
                return ResponseEntity.ok(new ApiResponse(true, "Edit account successful"));
            }
            if (userService.isEmailExist(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(false, "Email already exists"));
            }
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            userService.saveUser(user);
            return ResponseEntity.ok(new ApiResponse(true, "Edit account successful"));
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lay duong dan anh de tai ve
    @GetMapping("/api/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("public_id") String publicId) throws IOException {
        try {
            // Lay url tren duong dan anh de tai ve
            String imageUrl = cloudinaryService.getImageUrl(publicId);
            // doi duong link thanh bit
            RestTemplate restTemplate = new RestTemplate();
            byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
            ByteArrayResource resource = new ByteArrayResource(imageBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg");
            headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(imageBytes.length)
                    .body(resource);
        } catch (HttpClientErrorException e) {
            // Xử lý các lỗi từ RestTemplate
            throw new CustomException(ErrorCode.DOWNLOAD_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PostMapping("/api/relationship")
    public ResponseEntity<?> relationship(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            Long userId = payload.get("userId") != null ? Long.parseLong(payload.get("userId").toString()) : null;
            Long status = payload.get("status") != null ? Long.parseLong(payload.get("status").toString()) : null;

            if (userId == null) {
                return new ResponseEntity<>(new ApiResponse(false, "userId is required"), HttpStatus.BAD_REQUEST);
            }

            // Kiểm tra status và xử lý khi status là null
            if (status == null) {
                return new ResponseEntity<>(new ApiResponse(false, "status is required"), HttpStatus.BAD_REQUEST);
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User userOne = userService.findByEmail(userDetails.getUsername());
            User userTwo = userService.findById(userId);

            Relationship relationship = relationalService.findRelationship(userOne, userTwo);
            if (relationship == null) {
                relationship = new Relationship();
            }
            relationship.setUserOne(userOne);
            relationship.setUserTwo(userTwo);
            relationship.setStatus(statusRelationshipService.findById(status));
            relationalService.addUser(relationship);

            if (status == 1L) {
                String message = "Bạn nhận được lời mời kết bạn từ " + userOne.getFirstName() + " "
                        + userOne.getLastName();

                createNotification(userTwo, userOne, message);
            }

            boolean success = true;
            long newStatus = status;

            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("newStatus", newStatus);
            response.put("relationship", relationship);

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @MessageMapping("/friend.add")
    @SendTo("/queue/addfriend")
    public Notification notificationAddFriend(@Payload Map<String, String> idUserMap, Principal principal) {
        try {
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
        } catch (CustomException e) {
            // Log lỗi và ném lại
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PostMapping("/api/uploaduserimg")
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
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy danh sách thông báo
    @GetMapping("/api/notifications")
    public ResponseEntity<?> getNotifications(Principal principal) {
        try {
            Map<String, Object> response = new HashMap<>();
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            List<Notification> notifications = notificationService.findByUser(user);
            response.put("notifications", notifications);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Chấp nhận yêu cầu kết bạn
    @PostMapping("/api/acceptFriendRequest")
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
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xóa bạn
    @PostMapping("/api/deleteFriend")
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
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Kết bạn
    @PostMapping("/api/addFriend")
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
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy danh sách bạn bè
    @GetMapping("/api/friends")
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
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy danh sách không là bạn bè
    @GetMapping("/api/notFriend")
    public ResponseEntity<?> notfriends(Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            List<User> notFriends = relationalService.findNotFriends(user);
            return ResponseEntity.ok(notFriends);
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Đếm số lượng bạn bè - LƯU Ý STATUS
    @GetMapping("api/mutualFriend")
    public ResponseEntity<?> countFriend(@RequestParam Long friendId, Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            int count = relationalService.countMutualFriends(user, friendId);
            Map<String, Integer> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy phần About (người dùng mô tả thêm về bản thân)
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
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/countfriend")
    public ResponseEntity<?> countFriend(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User loggedInUser = userService.findByEmail(userDetails.getUsername());
            int countFriend = relationshipService.countfriend(loggedInUser, statusRelationshipService.findById(1L));
            List<Relationship> relationships = relationshipService.findRelationshipPending(loggedInUser);
            response.put("loggedInUser", loggedInUser);
            response.put("relationships", relationships);
            response.put("countFriend", countFriend);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy số lượng thông báo chưa xem
    @GetMapping("/api/countNotificationsIsCheck")
    public ResponseEntity<Map<String, Object>> getNotificationsIsCheck(Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User loggedInUser = userService.findByEmail(userDetails.getUsername());
            int unreadCount = notificationService.countUncheckedNotifications(loggedInUser);
            Map<String, Object> response = new HashMap<>();
            response.put("unreadCount", unreadCount);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } catch (CustomException e) {
            ApiResponse apiResponse = new ApiResponse(false, e.getErrorCode().getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("apiResponse", apiResponse);
            return new ResponseEntity<>(response, e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    // Lấy user
    @GetMapping("/api/getuser")
    public ResponseEntity<?> getUser(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findById(id);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Lấy mối quan hệ - tín hiệu có phải user logged - user đối diện
    @GetMapping("/api/getrelationship")
    public ResponseEntity<?> getRelationship(@RequestParam Long userId, Principal principal) {
        try {
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
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void createNotification(User user, User userSend, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(user);
        notification.setUserSend(userSend);
        notification.setChecked(false);
        notification.setUrl("/listfriend");
        notificationService.saveNotification(notification);
    }
}
