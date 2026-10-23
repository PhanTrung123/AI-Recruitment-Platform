package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.dto.JobDTO;
import com.quangtrung.HireAI.entity.Job;
import com.quangtrung.HireAI.repository.JobDomainRepository;
import com.quangtrung.HireAI.repository.JobRepository;
import com.quangtrung.HireAI.repository.MyJobRepository;
import com.quangtrung.HireAI.repository.SkillJobRepository;
import com.quangtrung.HireAI.service.JobService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    SkillJobRepository skillJobRepository;

    @Autowired
    JobDomainRepository jobDomainRepository;

    @Autowired
    MyJobRepository myJobRepository;

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
    public List<Job> findAll() {
        return jobRepository.findAll();
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

    @Override
    public List<Job> findBySkill(Long skillId) {
        return jobRepository.findBySkill(skillId);
    }

    @Override
    public List<Job> findByExpertise(Long expertisiesId) {
        return jobRepository.findByExpertise(expertisiesId);
    }

    @Override
    public List<Job> findByTitle(Long titleId) {
        return jobRepository.findByTitle(titleId);
    }

    @Override
    public List<Job> findByCity(com.quangtrung.HireAI.enums.City city) {
        return jobRepository.findByCity(city);
    }

    @Override
    public long count() {
        return jobRepository.count();
    }

    @Override
    public List<JobDTO> getJobsByUserId(Long userId) {
        List<Job> jobs = jobRepository.getJobsByUserId(userId);

        return jobs.stream().map(job -> {
            JobDTO dto = new JobDTO();

            BeanUtils.copyProperties(job, dto);

            if (job.getCompany() != null) dto.setCompany_id(job.getCompany().getCompany_id());
            if (job.getExpertise() != null) dto.setExpertisies_id(job.getExpertise().getExpertisies_id());
            if (job.getTitle() != null) dto.setTitle_id(job.getTitle().getTitle_id());
            long count = jobRepository.countByJobId(job.getJob_id());
            dto.setApplicationCount(count);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteJobCompletely(Long jobId) {
        skillJobRepository.deleteByJobId(jobId);
        jobDomainRepository.deleteByJobId(jobId);
        myJobRepository.deleteByJobId(jobId);

        jobRepository.deleteById(jobId);
    }


}
