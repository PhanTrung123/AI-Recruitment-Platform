package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Expertises")
public class Expertise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expertisies_id;
    private String expertiseName, description;

    @OneToMany(mappedBy = "expertise")
    private List<Job> jobs;


}
