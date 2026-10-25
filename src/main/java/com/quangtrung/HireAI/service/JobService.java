package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.dto.ApplicationDTO;
import com.quangtrung.HireAI.dto.JobDTO;
import com.quangtrung.HireAI.entity.Job;
import com.quangtrung.HireAI.enums.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface JobService {
    public Job save(Job job);

    Page<Job> findPaginated(Pageable pageable);

    Page<Job> findByJobNameContaining(String name, Pageable pageable);

    Optional<Job> findById(Long id);

    List<Job> findAll();

    Optional<Job> findByIdWithRelations(Long id);

    void deleteById(Long id);

    List<Job> searchJobs(String keyword, City city);

    java.util.List<Job> findByJobDomain(Long domainId);

    List<Job> findByCompanyId(Long companyId);

    List<Job> findBySkill(Long skillId);

    List<Job> findByExpertise(Long expertisiesId);

    List<Job> findByTitle(Long titleId);

    List<Job> findByCity(City city);

    long count();

    List<JobDTO> getJobsByUserId(Long userId);

    void deleteJobCompletely(Long jobId);

    List<ApplicationDTO> getApplicationsByRecruiter(Long recruiterUserId, String status, String keyword);

    public void updateApplicationStatus(Long myJobId, String status);

}
