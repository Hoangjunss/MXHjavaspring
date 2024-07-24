package com.baconbao.mxh.Models.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "UserAbout")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserAbout {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdUser")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAbout")
    private About about;

    @Column(name = "description")
    private String description;
}
