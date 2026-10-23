package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.SkillJob;
import com.quangtrung.HireAI.repository.SkillJobRepository;
import com.quangtrung.HireAI.service.SkillJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkillJobServiceImpl implements SkillJobService {

    @Autowired
    SkillJobRepository skillJobRepository;


    @Override
    public SkillJob save(SkillJob skillJob) {
        return skillJobRepository.save(skillJob);
    }

    @Override
    @Transactional
    public void deleteByJobId(Long jobId) {
        skillJobRepository.deleteByJobId(jobId);
    }
}
