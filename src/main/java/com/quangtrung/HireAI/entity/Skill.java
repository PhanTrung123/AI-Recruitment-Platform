package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skill_id;
    private String skillName, description;

    @OneToMany(mappedBy = "skill")
    private List<SkillJob> skillJobs;
}
