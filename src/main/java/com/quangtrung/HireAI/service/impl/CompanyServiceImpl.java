package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.dto.CompanyDTO;
import com.quangtrung.HireAI.entity.Company;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.repository.CompanyRepository;
import com.quangtrung.HireAI.service.CompanyService;
import com.quangtrung.HireAI.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserService userService;

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company saveFromDTO(CompanyDTO dto) {
        Company entity = new Company();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getUserId() != null) {
            User user = userService.findById(dto.getUserId()).orElse(null);
            entity.setUser(user);
        }
        return companyRepository.save(entity);
    }

    @Override
    public Page<Company> findPaginated(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    @Override
    public Page<Company> findByNameContaining(String name, Pageable pageable) {
        return companyRepository.findByNameContaining(name, pageable);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public Company findByUserId(Long userId) {
        return companyRepository.findByUserId(userId);
    }
}
