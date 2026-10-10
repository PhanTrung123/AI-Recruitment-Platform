package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.Skill;
import com.quangtrung.HireAI.repository.SkillRepository;
import com.quangtrung.HireAI.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    SkillRepository skillRepository;

    @Override
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public Page<Skill> findPaginated(Pageable pageable) {
        return skillRepository.findAll(pageable);
    }

    @Override
    public Page<Skill> findBySkillNameContaining(String name, Pageable pageable) {
        return skillRepository.findBySkillNameContaining(name, pageable);
    }

    @Override
    public Optional<Skill> findById(Long id) {
        return skillRepository.findById(id);
    }

    @Override
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        skillRepository.deleteById(id);
    }


}
