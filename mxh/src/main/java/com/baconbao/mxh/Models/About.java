package com.baconbao.mxh.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
   @GeneratedValue(strategy = GenerationType.IDENTITY)/* cho phép id tăng tự động */
   @Column(name="IdAbout")
   private Long Id;
   @Column(name="Name")
   private String Name;
   @Column(name="Describes")
   private String Describes;    
}
