package com.quangtrung.HireAI.entity;

import com.quangtrung.HireAI.enums.City;
import com.quangtrung.HireAI.enums.Level;
import com.quangtrung.HireAI.enums.WorkType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "Jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long job_id;
    private String jobName;
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean accepted;
    private Double salary;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    private LocalDate expiredAt;

    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @Enumerated(EnumType.STRING)
    private Level level;
    
    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<MyJob> myJobs;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<SkillJob> skillJobs;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    private List<JobDomain> jobDomains;

    @Enumerated(EnumType.STRING)
    private City city;

    @ManyToOne
    @JoinColumn(name = "expertisies_id")
    private Expertise expertise;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;

}
