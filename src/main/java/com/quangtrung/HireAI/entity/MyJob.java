package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "My_jobs")
public class MyJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myJob_id;
    private String status;
    private LocalDateTime appliedAt;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "cvs_id")
    private CV cv;
}
