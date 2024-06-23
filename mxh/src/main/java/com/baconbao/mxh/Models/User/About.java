package com.baconbao.mxh.Models.User;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="About")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class About {
   @Id/* dùng để cho biết thằng nào là khóa chính */
   @Column(name="IdAbout")
   private Long Id;
   @Column(name="Name")
   private String Name;

   @OneToMany(mappedBy = "about", cascade = CascadeType.ALL,  orphanRemoval = true)
   private List<UserAbout> userAbout = new ArrayList<>();
}
