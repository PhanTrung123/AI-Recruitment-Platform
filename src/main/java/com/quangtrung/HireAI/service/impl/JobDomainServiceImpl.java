package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.JobDomain;
import com.quangtrung.HireAI.repository.JobDomainRepository;
import com.quangtrung.HireAI.service.JobDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobDomainServiceImpl implements JobDomainService {

    @Autowired
    JobDomainRepository jobDomainRepository;

    @Override
    public JobDomain save(JobDomain jobDomain) {
        return jobDomainRepository.save(jobDomain);
    }
}
