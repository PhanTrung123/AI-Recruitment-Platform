package com.quangtrung.HireAI.entity;

import com.quangtrung.HireAI.enums.CompanyStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private CompanyStatus status = CompanyStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "company")
    private List<Job> jobs;
}
