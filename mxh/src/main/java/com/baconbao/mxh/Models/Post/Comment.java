package com.baconbao.mxh.Models.Post;

import com.baconbao.mxh.Models.User.User;

import jakarta.persistence.*;
import lombok.*;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @Column(name = "idComment")
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_send_id", referencedColumnName = "IdUser")
    private User userSend;
    private String content;
    
}
