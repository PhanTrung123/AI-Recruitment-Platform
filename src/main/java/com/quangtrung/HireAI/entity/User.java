package com.quangtrung.HireAI.entity;

import com.quangtrung.HireAI.enums.Gender;
import com.quangtrung.HireAI.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String userName;

    private String email;

    private String password;

    private String phone;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    private String personalLink;

    @OneToMany(mappedBy = "user")
    private List<Blog> blogs;

    @OneToMany(mappedBy = "user")
    private List<CV> cvs;
}
