package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.Title;
import com.quangtrung.HireAI.repository.TitleRepository;
import com.quangtrung.HireAI.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitleServiceImpl implements TitleService {

    @Autowired
    TitleRepository titleRepository;

    @Override
    public Title save(Title title) {
        return titleRepository.save(title);
    }

    @Override
    public Page<Title> findPaginated(Pageable pageable) {
        return titleRepository.findAll(pageable);
    }

    @Override
    public Page<Title> findByTitleNameContaining(String name, Pageable pageable) {
        return titleRepository.findByTitleNameContaining(name, pageable);
    }

    @Override
    public Optional<Title> findById(Long id) {
        return titleRepository.findById(id);
    }

    @Override
    public List<Title> findAll() {
        return titleRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        titleRepository.deleteById(id);
    }
}
