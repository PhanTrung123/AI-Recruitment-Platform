package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.JobDomain;
import com.quangtrung.HireAI.repository.JobDomainRepository;
import com.quangtrung.HireAI.service.JobDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobDomainServiceImpl implements JobDomainService {

    @Autowired
    JobDomainRepository jobDomainRepository;

    @Override
    public JobDomain save(JobDomain jobDomain) {
        return jobDomainRepository.save(jobDomain);
    }

    @Override
    @Transactional
    public void deleteByJobId(Long jobId) {
        jobDomainRepository.deleteByJobId(jobId);
    }
}
