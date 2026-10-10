package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.Expertise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertiseRepository extends JpaRepository<Expertise, Long> {
    Page<Expertise> findByExpertiseNameContaining(String name, Pageable pageable);
}
