package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.Blog;
import com.quangtrung.HireAI.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    public Blog save(Blog blog);

    Page<Blog> findPaginated(Pageable pageable);

    Page<Blog> findByTitleContaining(String title, Pageable pageable);

    List<Category> findAll();

    Optional<Blog> findById(Long id);

    void deleteById(Long id);

}
