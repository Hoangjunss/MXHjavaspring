package com.baconbao.mxh.Models.Post;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table (name="Interact")
@NoArgsConstructor
@AllArgsConstructor
public class Interact {
    @Id
    @Column(name="idInteract")
    private Long id;
    
    private String interactType;

    @OneToMany(mappedBy = "interactionType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interaction> interactions=new ArrayList<>();
}
    


