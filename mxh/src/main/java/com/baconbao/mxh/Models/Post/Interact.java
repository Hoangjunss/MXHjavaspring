package com.baconbao.mxh.Models.Post;

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
}
    


