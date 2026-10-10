package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Domains")

public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long domain_id;
    private String domainName, description;



    @OneToMany(mappedBy = "domain")
    private List<JobDomain> jobDomains;

}
