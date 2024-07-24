package com.baconbao.mxh.Models.Post;

import com.baconbao.mxh.Models.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interaction {
    @Id
    @Column(name = "idInteraction")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IdUser")
    private User user;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IdPost")
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="idInteract")
    @JsonIgnore
    private Interact interactionType;

}
