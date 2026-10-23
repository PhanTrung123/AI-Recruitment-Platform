package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.JobDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface JobDomainRepository extends JpaRepository<JobDomain, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM JobDomain jd WHERE jd.job.job_id = :jobId")
    void deleteByJobId(@Param("jobId") Long jobId);

}
