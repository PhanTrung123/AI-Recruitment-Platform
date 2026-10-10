package com.quangtrung.HireAI.repository;

import com.quangtrung.HireAI.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByJobNameContaining(String name, Pageable pageable);

    @Query("""
                SELECT j FROM Job j
                LEFT JOIN FETCH j.company
                LEFT JOIN FETCH j.expertise
                LEFT JOIN FETCH j.title
                WHERE j.job_id = :id
            """)
    Optional<Job> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT DISTINCT j FROM Job j JOIN j.jobDomains jd JOIN FETCH j.company WHERE jd.domain.domain_id = :domainId")
    List<Job> findByJobDomains(@Param("domainId") Long domainId);

    @Query("SELECT j FROM Job j JOIN FETCH j.company WHERE j.company.company_id = :companyId")
    List<Job> findByCompanyId(@Param("companyId") Long companyId);

}
