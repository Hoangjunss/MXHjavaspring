package com.baconbao.mxh.Controller.Posts;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.baconbao.mxh.Models.Image;
import com.baconbao.mxh.Models.Post;
import com.baconbao.mxh.Services.Service.ImageService;
import com.baconbao.mxh.Services.Service.PostService;

@Controller
public class PostsController {
    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;


    /* @GetMapping("/uploadpost")
    public String showUploadPostPage(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "uploadpost";
    } */

    @GetMapping("/uploadpost")
    public String uploadPost() {
        Post post = new Post();
        long lng = Long.parseLong("3519154021454530979");
        Image img = imageService.findById(lng);
        LocalDateTime expiryTime = LocalDateTime.now();
        post.setId(postService.getGenerationId());
        post.setContent("Date");
        post.setCreateAt(expiryTime);
        post.setUpdateAt(expiryTime);
        post.setStatus("hide");
        post.setImage(img);
        postService.save(post);
        return "redirect:/index";
    }
}
