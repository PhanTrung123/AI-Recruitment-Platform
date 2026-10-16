package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_id;
    private String name;
    private String logo;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private String website;
    private String size;
    private String address;
    private String country;

    @OneToMany(mappedBy = "company")
    private List<Job> jobs;
}
