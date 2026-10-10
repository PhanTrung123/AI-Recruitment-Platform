package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Titles")
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long title_id;

    private String titleName;

    @Column(columnDefinition = "TEXT")
    private String description;
}
