package com.quangtrung.HireAI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;
    private String categoryName, description;


    @OneToMany(mappedBy = "category")
    private List<Blog> blogs;
}
