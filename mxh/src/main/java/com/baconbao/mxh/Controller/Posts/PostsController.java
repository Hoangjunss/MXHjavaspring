package com.baconbao.mxh.Controller.Posts;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.baconbao.mxh.DTO.ImageDTO;
import com.baconbao.mxh.DTO.PostDTO;
import com.baconbao.mxh.Models.Image;
import com.baconbao.mxh.Models.Post;
import com.baconbao.mxh.Models.Status;
import com.baconbao.mxh.Services.CloudinaryService;
import com.baconbao.mxh.Services.Service.ImageService;
import com.baconbao.mxh.Services.Service.PostService;
import com.baconbao.mxh.Services.Service.StatusService;

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

    @GetMapping("/uploadpost")
    public String showUploadPostPage(Model model) {
        List<Status>status=statusService.findAll();
        model.addAttribute("status", status);
        return "insertimage";
    }

    @GetMapping("/getPosts")
    public ResponseEntity<List<Map<String, Object>>> getPosts() {
        Status status = statusService.findById(2);
        List<Post> posts = postService.findByStatus(status);

        List<Map<String, Object>> response = posts.stream().map(post -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("id", post.getId());
            postMap.put("content", post.getContent());
            postMap.put("imageUrl", post.getImage().getUrlImage());
            return postMap;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

   @PostMapping("/uploadpost")
public String uploadPost(Model model,
                         @RequestParam("content") String content,
                         @RequestParam("StatusId") Long status,
                         @RequestParam("image") MultipartFile image,
                         RedirectAttributes redirectAttributes) {
    try {
        // Tạo đối tượng Post và thiết lập nội dung và trạng thái
        Post post = new Post();
        Status statusPost = statusService.findById(status);
        post.setContent(content);
        post.setStatus(statusPost);

            // Kiểm tra xem tệp tin ảnh có rỗng không
            if (image.isEmpty()) {
                redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
                return "redirect:/uploadpost-form";
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
