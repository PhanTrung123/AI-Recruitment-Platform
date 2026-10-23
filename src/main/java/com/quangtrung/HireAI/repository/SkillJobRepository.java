package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.SkillJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SkillJobRepository extends JpaRepository<SkillJob, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM SkillJob sj WHERE sj.job.job_id = :jobId")
    void deleteByJobId(@Param("jobId") Long jobId);
}
