package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.entity.Blog;
import com.quangtrung.HireAI.entity.Category;
import com.quangtrung.HireAI.repository.BlogRepository;
import com.quangtrung.HireAI.repository.CategoryRepository;
import com.quangtrung.HireAI.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Blog save(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Page<Blog> findPaginated(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> findByTitleContaining(String title, Pageable pageable) {
        return blogRepository.findByTitleContaining(title, pageable);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Blog> findById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }
}
