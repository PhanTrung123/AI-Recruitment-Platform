package com.quangtrung.HireAI.service.impl;

import com.quangtrung.HireAI.dto.CompanyDTO;
import com.quangtrung.HireAI.dto.CompanyWithSkillsDTO;
import com.quangtrung.HireAI.entity.Company;
import com.quangtrung.HireAI.entity.Job;
import com.quangtrung.HireAI.entity.SkillJob;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.repository.CompanyRepository;
import com.quangtrung.HireAI.service.CompanyService;
import com.quangtrung.HireAI.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public CompanyDTO getCompanyByRecruiterId(Long userId) {
        return companyRepository.findByUserIdAndRole(userId).map(company -> {
            CompanyDTO dto = new CompanyDTO();
            dto.setCompany_id(company.getCompany_id());
            dto.setName(company.getName());
            dto.setLogo(company.getLogo());
            dto.setDescription(company.getDescription());
            dto.setWebsite(company.getWebsite());
            dto.setAddress(company.getAddress());
            dto.setSize(company.getSize());
            dto.setSlogan(company.getSlogan());
            dto.setFoundedYear(company.getFoundedYear());
            dto.setIndustry(company.getIndustry());
            dto.setMapUrl(company.getMapUrl());
            dto.setBenefits(company.getBenefits());
            return dto;
        }).orElse(null);
    }

    @Override
    @Transactional
    public boolean updateCompanyField(Long userId, String field, String value) {
        Optional<Company> optCompany = companyRepository.findByUserIdAndRole(userId);
        if (optCompany.isEmpty()) return false;

        Company company = optCompany.get();
        String cleanValue = (value != null) ? value.trim() : "";

        switch (field) {
            case "name":
                company.setName(cleanValue);
                break;
            case "logo":
                company.setLogo(cleanValue);
                break;
            case "slogan":
                company.setSlogan(cleanValue);
                break;
            case "size":
                company.setSize(cleanValue);
                break;
            case "foundedYear":
                try {
                    company.setFoundedYear(cleanValue.isEmpty() ? null : Integer.parseInt(cleanValue));
                } catch (NumberFormatException e) {
                    return false;
                }
                break;
            case "industry":
                company.setIndustry(cleanValue);
                break;
            case "website":
                company.setWebsite(cleanValue);
                break;
            case "address":
                company.setAddress(cleanValue);
                if (!cleanValue.isEmpty()) {
                    String encodedAddress = java.net.URLEncoder.encode(cleanValue, java.nio.charset.StandardCharsets.UTF_8);
                    String generatedMapUrl = "https://maps.google.com/maps?q=" + encodedAddress + "&t=&z=15&ie=UTF8&iwloc=&output=embed";
                    company.setMapUrl(generatedMapUrl);
                } else {
                    company.setMapUrl(null);
                }
                break;
            case "mapUrl":
                company.setMapUrl(cleanValue);
                break;
            case "description":
                company.setDescription(cleanValue);
                break;
            case "benefits":
                if (company.getBenefits() == null) {
                    company.setBenefits(new java.util.ArrayList<>());
                } else {
                    company.getBenefits().clear();
                }
                if (!cleanValue.isEmpty()) {
                    String[] items = cleanValue.split("\\r?\\n");
                    for (String item : items) {
                        if (!item.trim().isEmpty()) {
                            company.getBenefits().add(item.trim());
                        }
                    }
                }
                break;
            default:
                return false;
        }
        companyRepository.save(company);
        return true;
    }

    @Override
    public List<CompanyWithSkillsDTO> getTopHiringCompanies() {
        List<Company> companies = companyRepository.findTopHiringCompanies();
        List<CompanyWithSkillsDTO> result = new ArrayList<>();

        for (int i = 0; i < Math.min(6, companies.size()); i++) {
            Company company = companies.get(i);
            CompanyWithSkillsDTO dto = new CompanyWithSkillsDTO();
            dto.setCompany_id(company.getCompany_id());
            dto.setName(company.getName());
            dto.setLogo(company.getLogo());
            dto.setAddress(company.getAddress());
            dto.setJobCount(company.getJobs() != null ? company.getJobs().size() : 0);
            dto.setIsTopHiring(i == 0);

            // Aggregate skills from all jobs
            Map<String, Integer> skillFrequency = new HashMap<>();
            if (company.getJobs() != null) {
                for (Job job : company.getJobs()) {
                    if (job.getSkillJobs() != null) {
                        for (SkillJob skillJob : job.getSkillJobs()) {
                            if (skillJob.getSkill() != null) {
                                String skillName = skillJob.getSkill().getSkillName();
                                skillFrequency.put(skillName, skillFrequency.getOrDefault(skillName, 0) + 1);
                            }
                        }
                    }
                }
            }

            // Get top 6 skills by frequency
            List<String> topSkills = skillFrequency.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(6)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            dto.setTopSkills(topSkills);
            result.add(dto);
        }

        return result;
    }
}
