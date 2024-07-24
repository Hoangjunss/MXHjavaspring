package com.baconbao.mxh.Controller.Api.Posts;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baconbao.mxh.Config.Web.AppConstants;
import com.baconbao.mxh.DTO.ApiResponse;
import com.baconbao.mxh.DTO.InteractionDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Models.Post.Interact;
import com.baconbao.mxh.Models.Post.Interaction;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.User.Notification;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.CloudinaryService;
import com.baconbao.mxh.Services.Service.Post.CommentService;
import com.baconbao.mxh.Services.Service.Post.ImageService;
import com.baconbao.mxh.Services.Service.Post.InteractService;
import com.baconbao.mxh.Services.Service.Post.InteractionService;
import com.baconbao.mxh.Services.Service.Post.PostService;
import com.baconbao.mxh.Services.Service.User.NotificationService;
import com.baconbao.mxh.Services.Service.User.UserService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.API_BASE_PATH)
public class ApiPostsController {
    private PostService postService;
    private ImageService imageService;
    private CloudinaryService cloudinaryService;
    private UserDetailsService userDetailsService;
    private UserService userService;
    private CommentService commentService;
    private InteractService interactService;
    private InteractionService interactionService;
    private NotificationService notificationService;

    @PostMapping("/notificationsischecked")
    public ResponseEntity<?> markNotificationsAsRead(Principal principal) {
      
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            notificationService.markAllNotificationAsRead(user);
            return ResponseEntity.ok().build();
      
    }

    @PostMapping("/uploadpostuser")
    public ResponseEntity<?> uploadpost(@RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image, Principal principal) {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            Post post = Post.builder()
                            .content(content)
                            .isActive(true)
                            .createAt(localDateTime)
                            .updateAt(localDateTime)
                            .user(user)
                            .build();
            // Tạo đối tượng Image và lưu URL ảnh
            // Kiểm tra xem tệp tin ảnh có rỗng không
            if (image != null) {
                Image img = new Image();
                Map<String, Object> resultMap = cloudinaryService.upload(image);
                String imageUrl = (String) resultMap.get("url");
                img.setUrlImage(imageUrl);
                imageService.saveImage(img); // Lưu đối tượng Image
                Image tmpImg = imageService.findByImage(img.getUrlImage());
                post.setImage(tmpImg);
            }
            postService.save(post); // Lưu đối tượng Post
            return ResponseEntity.ok(new ApiResponse(true, "Post uploaded successfully"));
        } catch (CustomException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getErrorCode().getMessage()),
                    e.getErrorCode().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse(false, "An unexpected error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/hidepost")
    public ResponseEntity<?> markNotificationsAsRead(@RequestParam("id") long id) {
      
            Post post = postService.findById(id);
            post.setActive(false); // set active
            LocalDateTime localDateTime = LocalDateTime.now();
            post.setUpdateAt(localDateTime);
            postService.save(post);
            return ResponseEntity.ok().build();
       
    }

    @PostMapping("/savepost")
    public ResponseEntity<?> savepost(@RequestParam("id") long id, @RequestParam("content") String content) {
      
            Post post = postService.findById(id); // tìm post theo id
            post.setContent(content);// gán nội dung mới
            post.setActive(true); // gán trạng thái active
            LocalDateTime localDateTime = LocalDateTime.now();
            post.setUpdateAt(localDateTime);
            postService.save(post); // lưu post đã chỉnh sửa vào database
            return ResponseEntity.ok().build();
     
    }

    // Đăng comment của post
    @PostMapping("/commenter")
    public ResponseEntity<?> postComment(@RequestBody Map<String, Object> payload, Principal principal) {
        Map<String, Object> response = new HashMap<>();
      
            String content = (String) payload.get("content");
            Long idPost = Long.valueOf(payload.get("postId") + "");

            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());

            Post post = postService.findById(idPost);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
            Comment comment = Comment.builder()
                                     .content(content)
                                     .userSend(user)
                                     .createAt(LocalDateTime.now())
                                     .build();
            List<Comment> comments = post.getComments();
            comments.add(comment);
            commentService.save(comment);
            post.setComments(comments);
            postService.save(post);
            if (user.getId() != post.getUser().getId()) {
                String message = user.getFirstName() + " " + user.getLastName() + " commented on your post";
                notificationService.createNotification(post.getUser(), user, message, "/post?id="+post.getId());
            }
            response.put("success", true);
            response.put("comment", comment);
            return ResponseEntity.ok(response);
    }

    // Lỗi truy vấn LIKE
    @PostMapping("/interact")
    public ResponseEntity<?> handleInteraction(@RequestBody InteractionDTO interactionDTO, Principal principal) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            Post post = postService.findById(interactionDTO.getPostId());
            Interact interact = interactService.findById(interactionDTO.getReactionId());
            Interaction interaction = interactionService.findByPostAndUser(post, user);
            if(interaction!=null){
                interaction.setInteractionType(interact);
                interactionService.saveInteraction(interaction);
                String message = user.getFirstName()+" "+user.getLastName()+" has interaction on your post ";
                notificationService.createNotification(post.getUser(), user, message, "/post?id="+post.getId());
                return ResponseEntity.ok(new ApiResponse(true, "Reaction updated successfully"));
            }else{
                interaction = Interaction.builder()
                                         .interactionType(interact)
                                         .post(post)
                                         .user(user)
                                         .build();
                interactionService.saveInteraction(interaction);
                String message = user.getFirstName()+" "+user.getLastName()+" has interaction on your post ";
                notificationService.createNotification(post.getUser(), user, message, "/post?id="+post.getId());
                return ResponseEntity.ok(new ApiResponse(true, "Reaction saved successfully"));
            }
    }

    

    @GetMapping("/interaction")
    public ResponseEntity<?> getInteraction(@RequestParam Long id, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        Interaction interaction = interactionService.findById(id);
        InteractionDTO interactionDTO = new InteractionDTO(interaction.getInteractionType().getId(), null);
        response.put("interac", interactionDTO);
        return ResponseEntity.ok(response);
    }

    // Lấy ra tất cả bài viết
    @GetMapping("/post")
    public ResponseEntity<?> post(@RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        List<Post> posts = new ArrayList<>();
      
            if (id == null) {
                posts = postService.findByActive(true);
            } else {
                posts = postService.findByUserPosts(userService.findById(id));
            }
            response.put("posts", posts);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/postdetails")
    public ResponseEntity<?> postDetails(@RequestParam Long id) {
        Post posts = postService.findById(id);
            return ResponseEntity.ok(posts);
    }


    @GetMapping("/notifications") // Lấy ra tất cả thông báo
    public ResponseEntity<?> notifications(Principal principal) {
       
            Map<String, Object> response = new HashMap<>();
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            List<Notification> notifications = notificationService.findByUser(user);
            response.put("notifications", notifications);
            return ResponseEntity.ok(response);
      
    }

    @GetMapping("/editpost")
    public ResponseEntity<?> editpost(@RequestParam("id") Long id, Principal principal) {
       
            Post post = postService.findById(id);
            return ResponseEntity.ok(post);
        
    }

    @GetMapping("/comment")
    public ResponseEntity<?> comment(@RequestParam Long id, Principal principal) {
        Map<String, Object> response = new HashMap<>();
            Post post = postService.findById(id);
            List<Comment> comments = post.getComments();
            comments.sort(Comparator.comparing(Comment::getCreateAt).reversed());
            response.put("comments", comments);
            return ResponseEntity.ok(response);
    }

    @GetMapping("/countinteraction")
    public ResponseEntity<?> countIneraction(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        Post post = postService.findById(id);
        Long countInteract = postService.countInteraction(post);
        response.put("countInteract", countInteract);
        return ResponseEntity.ok(response);
    }

}
