package com.baconbao.mxh.Controller.Controller.Posts;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PostsController {

    @GetMapping({ " ", " /" })
    public String getIndex() {
        return "index";
    }

    @GetMapping("/testpost")
    public String test() {
        return "editpost";
    }

    @GetMapping("/searchuser")
    public String searchUser() {
        return "searchuser";
    }

    @GetMapping("/post")
    public String post(@RequestParam Long id) {
        return "postdetails";
    }

}
