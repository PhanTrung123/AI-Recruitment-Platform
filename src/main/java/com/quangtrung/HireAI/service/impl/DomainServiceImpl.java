package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.Domain;
import com.quangtrung.HireAI.repository.DomainRepository;
import com.quangtrung.HireAI.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DomainServiceImpl implements DomainService {

    @Autowired
    DomainRepository domainRepository;

    @Override
    public Domain save(Domain domain) {
        return domainRepository.save(domain);
    }

    @Override
    public Page<Domain> findPaginated(Pageable pageable) {
        return domainRepository.findAll(pageable);
    }

    @Override
    public Page<Domain> findByDomainNameContaining(String name, Pageable pageable) {
        return domainRepository.findByDomainNameContaining(name, pageable);
    }

    @Override
    public Optional<Domain> findById(Long id) {
        return domainRepository.findById(id);
    }

    @Override
    public List<Domain> findAll() {
        return domainRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        domainRepository.deleteById(id);
    }
}
