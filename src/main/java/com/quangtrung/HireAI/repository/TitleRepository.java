package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.Title;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {
    Page<Title> findByTitleNameContaining(String name, Pageable pageable);
}
