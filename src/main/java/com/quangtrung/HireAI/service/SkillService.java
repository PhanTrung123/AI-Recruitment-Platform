package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    public Skill save(Skill skill);

    Page<Skill> findPaginated(Pageable pageable);

    Page<Skill> findBySkillNameContaining(String name, Pageable pageable);

    Optional<Skill> findById(Long id);

    List<Skill> findAll();

    void deleteById(Long id);

}
