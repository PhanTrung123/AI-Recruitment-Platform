package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CompanyService {
    public Company save(Company company);

    Page<Company> findPaginated(Pageable pageable);

    Page<Company> findByNameContaining(String name, Pageable pageable);

    List<Company> findAll();

    Optional<Company> findById(Long id);
    
    void deleteById(Long id);
}
