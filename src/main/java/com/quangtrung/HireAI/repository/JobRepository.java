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

    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.company LEFT JOIN FETCH j.jobDomains jd LEFT JOIN FETCH jd.domain WHERE j IN (SELECT j2 FROM Job j2 JOIN j2.jobDomains jd2 WHERE jd2.domain.domain_id = :domainId)")
    List<Job> findByJobDomains(@Param("domainId") Long domainId);

    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.company LEFT JOIN FETCH j.skillJobs sj LEFT JOIN FETCH sj.skill WHERE j.company.company_id = :companyId")
    List<Job> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.company LEFT JOIN FETCH j.skillJobs sj LEFT JOIN FETCH sj.skill WHERE j IN (SELECT j2 FROM Job j2 JOIN j2.skillJobs sj2 WHERE sj2.skill.skill_id = :skillId)")
    List<Job> findBySkill(@Param("skillId") Long skillId);

    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.company LEFT JOIN FETCH j.skillJobs sj LEFT JOIN FETCH sj.skill WHERE j.expertise.expertisies_id = :expertisiesId")
    List<Job> findByExpertise(@Param("expertisiesId") Long expertisiesId);

    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.company LEFT JOIN FETCH j.skillJobs sj LEFT JOIN FETCH sj.skill WHERE j.title.title_id = :titleId")
    List<Job> findByTitle(@Param("titleId") Long titleId);

    @Query("SELECT DISTINCT j FROM Job j LEFT JOIN FETCH j.company LEFT JOIN FETCH j.skillJobs sj LEFT JOIN FETCH sj.skill WHERE j.city = :city")
    List<Job> findByCity(@Param("city") com.quangtrung.HireAI.enums.City city);

    long count();

    @Query("SELECT j FROM Job j WHERE j.company.user.id = :userId")
    List<Job> getJobsByUserId(@Param("userId") Long userId);

    // Đếm số lượng bản ghi MyJob theo job_id của bảng Job
    @Query("SELECT COUNT(mj) FROM MyJob mj WHERE mj.job.job_id = :jobId")
    long countByJobId(@Param("jobId") Long jobId);
}
