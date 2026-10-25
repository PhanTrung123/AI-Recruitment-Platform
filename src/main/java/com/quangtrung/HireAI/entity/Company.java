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
    @Column(columnDefinition = "LONGTEXT")
    private String logo;
    private String slogan;

    private Integer foundedYear;
    private String industry;
    private String size;
    private String website;
    private String address;
    private String country;
    private String mapUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "company_benefits", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "benefit")
    private List<String> benefits;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private CompanyStatus status = CompanyStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "company")
    private List<Job> jobs;

}
