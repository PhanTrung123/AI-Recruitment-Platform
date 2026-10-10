package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.Expertise;
import com.quangtrung.HireAI.repository.ExpertiseRepository;
import com.quangtrung.HireAI.service.ExpertiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpertiseServiceImpl implements ExpertiseService {

    @Autowired
    ExpertiseRepository expertiseRepository;

    @Override
    public Expertise save(Expertise expertise) {
        return expertiseRepository.save(expertise);
    }

    @Override
    public Page<Expertise> findPaginated(Pageable pageable) {
        return expertiseRepository.findAll(pageable);
    }

    @Override
    public Page<Expertise> findByExpertiseNameContaining(String name, Pageable pageable) {
        return expertiseRepository.findByExpertiseNameContaining(name, pageable);
    }

    @Override
    public Optional<Expertise> findById(Long id) {
        return expertiseRepository.findById(id);
    }

    @Override
    public List<Expertise> findAll() {
        return expertiseRepository.findAll();

    }

    @Override
    public void deleteById(Long id) {
        expertiseRepository.deleteById(id);
    }
}
