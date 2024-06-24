package com.baconbao.mxh.Models.Post;

import java.time.LocalDateTime;

import com.baconbao.mxh.Models.User.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyComment {
    @Id
    @Column(name="IdReplyComment")
    private Long id;
    private User UserSend;
    private String content;
    private LocalDateTime createAt;
}
