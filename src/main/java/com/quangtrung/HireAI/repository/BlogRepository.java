package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByTitleContaining(String title, Pageable pageable);
}
