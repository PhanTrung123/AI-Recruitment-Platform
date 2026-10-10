package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DomainService {

    public Domain save(Domain domain);

    Page<Domain> findPaginated(Pageable pageable);

    Page<Domain> findByDomainNameContaining(String name, Pageable pageable);

    Optional<Domain> findById(Long id);

    List<Domain> findAll();

    void deleteById(Long id);
}
