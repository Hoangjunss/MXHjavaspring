package com.baconbao.mxh.Models.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="About")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class About {
   @Id/* dùng để cho biết thằng nào là khóa chính */
   @Column(name="IdAbout")
   private Long id;
   @Column(name="Name")
   private String name;
}
