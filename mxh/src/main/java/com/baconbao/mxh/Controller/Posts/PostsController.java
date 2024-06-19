package com.baconbao.mxh.Controller.Posts;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.baconbao.mxh.DTO.ImageDTO;
import com.baconbao.mxh.DTO.PostDTO;
import com.baconbao.mxh.Models.Image;
import com.baconbao.mxh.Models.Post;
import com.baconbao.mxh.Services.CloudinaryService;
import com.baconbao.mxh.Services.Service.ImageService;
import com.baconbao.mxh.Services.Service.PostService;

@Controller
public class PostsController {
    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/uploadpost")
    public String showUploadPostPage() {
        return "insertimage";
    }

    @PostMapping("/uploadpost")
    public String uploadPost(Model model, @RequestParam("content") String content,
            @RequestParam("status") String status,
            @RequestParam("image") MultipartFile image) throws Exception {
        Post post = new Post();
        post.setContent(content);
        post.setStatus(status);
        Image img = new Image();
        Map resultMap = cloudinaryService.upload(image);
        String imageUrl = (String) resultMap.get("url");
        img.setUrlImage(imageUrl);
        
        imageService.saveImage(img);
        post.setImage(img);
        postService.save(post);
        return "redirect:/index";
    }
}
