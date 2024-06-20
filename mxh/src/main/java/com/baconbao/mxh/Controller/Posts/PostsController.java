package com.baconbao.mxh.Controller.Posts;

import java.io.File;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.baconbao.mxh.Models.Image;
import com.baconbao.mxh.Models.Post;
import com.baconbao.mxh.Models.Status;
import com.baconbao.mxh.Models.User;
import com.baconbao.mxh.Services.CloudinaryService;
import com.baconbao.mxh.Services.Service.ImageService;
import com.baconbao.mxh.Services.Service.PostService;
import com.baconbao.mxh.Services.Service.StatusService;
import com.baconbao.mxh.Services.Service.UserService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
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

    @GetMapping("/uploadpost")
    public String showUploadPostPage(Model model) {
        List<Status> status = statusService.findAll();
        model.addAttribute("status", status);
        return "insertimage";
    }

    @GetMapping("/")
    public String getPosts(Model model) {
        List<Status>status=statusService.findAll();
        model.addAttribute("status", status);
        Status status1 = statusService.findById(1);
        List<Post> posts = postService.findByStatus(status1);
        model.addAttribute("posts", posts);
        return "index";
    }

    @PostMapping("/uploadpost")
    public String uploadPost(Model model,
            @RequestParam("content") String content,
            @RequestParam("StatusId") Long status,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes,
            Principal principal) {
        try {
            // Tạo đối tượng Post và thiết lập nội dung và trạng thái
            Post post = new Post();
            Status statusPost = statusService.findById(status);
            post.setContent(content);
            post.setStatus(statusPost);
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            User user = userService.findByEmail(userDetails.getUsername());
            post.setUser(user);

            // Kiểm tra xem tệp tin ảnh có rỗng không
            if (image.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
                return "redirect:/";
            }

            // Tải ảnh lên Cloudinary
            Map<String, Object> resultMap = cloudinaryService.upload(image);
            String imageUrl = (String) resultMap.get("url");

            // Tạo đối tượng Image và lưu URL ảnh
            Image img = new Image();
            img.setUrlImage(imageUrl);
            imageService.saveImage(img); // Lưu đối tượng Image
            Image tmpImg = imageService.findByImage(img.getUrlImage());
            // Thiết lập mối quan hệ giữa Post và Image
            post.setImage(tmpImg);
            postService.save(post); // Lưu đối tượng Post
            redirectAttributes.addFlashAttribute("message", "Upload successful");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Upload failed: " + e.getMessage());
        }
        return "redirect:/";
    }

}
