package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.JobDomain;

public interface JobDomainService {
    public JobDomain save(JobDomain jobDomain);

    void deleteByJobId(Long jobId);
}
