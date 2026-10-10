package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Skills_Job")

public class SkillJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillJob_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id")
    private Job job;
}
