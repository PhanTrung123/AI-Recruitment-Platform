package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "CVs")

public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cvs_id ;
    private String fileUrl;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "cv")
    private List<MyJob> myJobs;
}

