package com.baconbao.mxh.Controller.Posts;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.baconbao.mxh.DTO.ApiResponse;
import com.baconbao.mxh.DTO.InteractionDTO;
import com.baconbao.mxh.Exceptions.CustomException;
import com.baconbao.mxh.Exceptions.ErrorCode;
import com.baconbao.mxh.Models.Post.Comment;
import com.baconbao.mxh.Models.Post.Image;
import com.baconbao.mxh.Models.Post.Interact;
import com.baconbao.mxh.Models.Post.Interaction;
import com.baconbao.mxh.Models.Post.Post;
import com.baconbao.mxh.Models.Post.ReplyComment;
import com.baconbao.mxh.Models.Post.Status;
import com.baconbao.mxh.Models.User.Notification;
import com.baconbao.mxh.Models.User.User;
import com.baconbao.mxh.Services.CloudinaryService;
import com.baconbao.mxh.Services.Service.TestService;
import com.baconbao.mxh.Services.Service.Post.CommentService;
import com.baconbao.mxh.Services.Service.Post.ImageService;
import com.baconbao.mxh.Services.Service.Post.InteractService;
import com.baconbao.mxh.Services.Service.Post.InteractionService;
import com.baconbao.mxh.Services.Service.Post.PostService;
import com.baconbao.mxh.Services.Service.Post.ReplyCommentService;
import com.baconbao.mxh.Services.Service.Post.StatusService;
import com.baconbao.mxh.Services.Service.User.NotificationService;
import com.baconbao.mxh.Services.Service.User.RelationshipService;
import com.baconbao.mxh.Services.Service.User.StatusRelationshipService;
import com.baconbao.mxh.Services.Service.User.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class PostsController {
    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private InteractService interactService;
    @Autowired
    private InteractionService interactionService;
    @Autowired
    private ReplyCommentService replyCommentService;
    @Autowired
    private TestService testService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private StatusRelationshipService statusRelationshipService;

   

    @PostMapping("/notificationsischecked")
    public ResponseEntity<?> markNotificationsAsRead(Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            notificationService.markAllNotificationAsRead(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @PostMapping("/uploadpost")
    public String uploadPost(Model model,
            @RequestParam("content") String content, // tra ve html co bien la content voi du lieu la content (String)
                                                     // th:text="${content}"
            @RequestParam("StatusId") Long status, // tra ve html co bien la StatusId
            @RequestParam("image") MultipartFile image, // tra ve html co bien la Image
            RedirectAttributes redirectAttributes,
            Principal principal) { // Giong voi session khi login se luu username(email) va password cua user
        try {
            // Tạo đối tượng Post và thiết lập nội dung và trạng thái
            Post post = new Post();
            Status statusPost = statusService.findById(status);
            post.setContent(content);
            post.setStatus(statusPost);
            post.setActive(true);
            // dat ngay va gio tao post
            LocalDateTime localDateTime = LocalDateTime.now();
            post.setCreateAt(localDateTime);
            post.setUpdateAt(localDateTime);
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            post.setUser(user);

            // Tạo đối tượng Image và lưu URL ảnh
            // Kiểm tra xem tệp tin ảnh có rỗng không
            if (!image.isEmpty()) {
                Image img = new Image();
                Map<String, Object> resultMap = cloudinaryService.upload(image);
                String imageUrl = (String) resultMap.get("url");
                img.setUrlImage(imageUrl);
                imageService.saveImage(img); // Lưu đối tượng Image
                Image tmpImg = imageService.findByImage(img.getUrlImage());
                post.setImage(tmpImg);
            }
            postService.save(post); // Lưu đối tượng Post
            redirectAttributes.addFlashAttribute("message", "Upload successful");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Upload failed: " + e.getMessage());
        }
        return "redirect:/";
    }

    // An bai viet
    @PostMapping("/hidepost")
    public String hidePost(Model model, @RequestParam("id") long id) {
        // RequestParam: lay thuoc tinh cua id duoc post trong the form
        // Tim post theo id da post
        Post post = postService.findById(id);
        post.setActive(false); // set active
        LocalDateTime localDateTime = LocalDateTime.now();
        post.setUpdateAt(localDateTime);
        postService.save(post);
        return "redirect:/";
    }

    // Chỉnh sửa bài viết
    @GetMapping("/editpost")
    public String editPost(Model model, @RequestParam long id) {
        Post post = postService.findById(id); // tìm post theo id
        model.addAttribute("post", post); // truyền dữ liệu post qua view (editpost.html)
        return "editpost";
    }

    // Lưu bài viết đã chỉnh sửa
    @PostMapping("/savepost")
    public String savePost(Model model, @RequestParam("id") long id, @RequestParam("content") String content) {
        try {
            Post post = postService.findById(id); // tìm post theo id
            post.setContent(content);// gán nội dung mới
            post.setActive(true); // gán trạng thái active
            LocalDateTime localDateTime = LocalDateTime.now();
            post.setUpdateAt(localDateTime);
            postService.save(post); // lưu post đã chỉnh sửa vào database
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/testpost")
    public String test() {
        return "editpost";
    }

    // Đăng comment của post
    @PostMapping("/commenter")
    public ResponseEntity<?> postComment(@RequestBody Map<String, Object> payload, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            String content = (String) payload.get("content");
            Long idPost = Long.valueOf((String) payload.get("postId"));

            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());

            Post post = postService.findById(idPost);
            if (post == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
            }
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setUserSend(user);
            comment.setCreateAt(LocalDateTime.now());
            List<Comment> comments = post.getComments();
            comments.add(comment);
            commentService.save(comment);

            post.setComments(comments);
            postService.save(post);
            if (user.getId() != post.getUser().getId()) {
                Notification notification = new Notification();
                notification.setChecked(false);
                notification.setMessage(user.getFirstName() + " " + user.getLastName() + " commented on your post");
                notification.setUser(post.getUser());
                notification.setUrl("/postdetails");
                notificationService.saveNotification(notification);
            }
            response.put("success", true);
            response.put("comment", comment);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }

    // Lấy phần trả lời bình luận theo id
    @PostMapping("/postReplyComment")
    public String postReplyComment(@RequestParam("id") Long id, @RequestParam("content") String content,
            Principal principal) {
        // tìm comment được reply
        Comment comment = commentService.findById(id);
        // Tạo đối tượng reply, lưu những thông tin cần thiết
        ReplyComment replyComment = new ReplyComment();
        replyComment.setId(replyCommentService.getGenerationId());
        replyComment.setContent(content);
        LocalDateTime localDateTime = LocalDateTime.now();
        replyComment.setCreateAt(localDateTime);
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        User user = userService.findByEmail(userDetails.getUsername());
        replyComment.setUserSend(user);
        // Lưu thông tin
        List<ReplyComment> replyComments = comment.getReplyComment();
        replyComments.add(replyComment);
        replyCommentService.save(replyComment);
        comment.setReplyComment(replyComments);
        commentService.save(comment);
        return "redirect:/getComment";
    }

    // Lỗi truy vấn LIKE
    @PostMapping("/interact")
    public ResponseEntity<?> handleInteraction(@RequestBody InteractionDTO interactionDTO, Principal principal) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            Post post = postService.findById(interactionDTO.getPostId());
            Interact interact = interactService.findById(interactionDTO.getReactionId());
            Interaction interaction = new Interaction();
            interaction.setInteractionType(interact);
            interaction.setPost(post);
            interaction.setUser(user);
            interactionService.saveInteraction(interaction);
            return ResponseEntity.ok(new ApiResponse(true, "Reaction saved successfully"));
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @GetMapping("/searchuser")
    public String searchUser() {
        return "searchuser";
    }

    @PostMapping("/searchuser")
    public ResponseEntity<?> searchuser(@RequestBody String name) {
        try {
            List<com.baconbao.mxh.Models.test> id_users = testService.findByLastNameOrFirstName(name, name);
            return ResponseEntity.ok(id_users);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ABOUT_NOT_SAVED);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    @GetMapping({ "/", " " })
    public String getPosts(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());// Lấy ra email của người
                                                                                            // dùng đang đăng nhập
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        int countFriend = relationshipService.countfriend(user, statusRelationshipService.findById(2L));
        model.addAttribute("countFriend", countFriend);
        List<Notification> notifications = notificationService.findByUser(user);
        model.addAttribute("notifications", notifications);
        int unreadCount = notificationService.countUncheckedNotifications(user);
        model.addAttribute("unreadCount", unreadCount);
        List<Status> status = statusService.findAll();
        model.addAttribute("status", status);
        Status status1 = statusService.findById(1); // LUU Y TIM STATUS
        List<Post> posts = postService.findByActiveAndStatus(true, status1);
        model.addAttribute("posts", posts);
        return "index";
    }
}
