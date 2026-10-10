package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
    Page<Domain> findByDomainNameContaining(String name, Pageable pageable);
}
