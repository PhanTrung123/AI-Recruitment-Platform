package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.Expertise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ExpertiseService {

    public Expertise save(Expertise expertise);

    Page<Expertise> findPaginated(Pageable pageable);

    Page<Expertise> findByExpertiseNameContaining(String name, Pageable pageable);

    Optional<Expertise> findById(Long id);

    List<Expertise> findAll();

    void deleteById(Long id);
}
