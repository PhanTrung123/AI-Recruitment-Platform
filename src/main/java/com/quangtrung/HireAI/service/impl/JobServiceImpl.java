package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.Job;
import com.quangtrung.HireAI.repository.JobRepository;
import com.quangtrung.HireAI.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobRepository jobRepository;

    @Override
    public Job save(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Page<Job> findPaginated(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }

    @Override
    public Page<Job> findByJobNameContaining(String name, Pageable pageable) {
        return jobRepository.findByJobNameContaining(name, pageable);
    }

    @Override
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }

    @Override
    public Optional<Job> findByIdWithRelations(Long id) {
        return jobRepository.findByIdWithRelations(id);
    }

    @Override
    public void deleteById(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> findByJobDomain(Long domainId) {
        return jobRepository.findByJobDomains(domainId);
    }

    @Override
    public List<Job> findByCompanyId(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }
}
