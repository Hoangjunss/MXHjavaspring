package com.baconbao.mxh.DTO;

import java.time.LocalDateTime;

import com.baconbao.mxh.Models.Post.Image;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private long id;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private Image image;
}
