package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    public Category save(Category category);

    Page<Category> findPaginated(Pageable pageable);

    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    void deleteById(Long id);
}
