package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Job_Domains")

public class JobDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long JobDomain_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id")
    private Domain domain;
}
