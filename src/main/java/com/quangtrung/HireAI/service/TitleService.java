package com.quangtrung.HireAI.service;

import com.quangtrung.HireAI.entity.Title;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TitleService {

    public Title save(Title title);

    Page<Title> findPaginated(Pageable pageable);

    Page<Title> findByTitleNameContaining(String name, Pageable pageable);

    Optional<Title> findById(Long id);

    List<Title> findAll();

    void deleteById(Long id);
}
