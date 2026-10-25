package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.*;
import com.quangtrung.HireAI.enums.City;
import com.quangtrung.HireAI.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ClientController {

    @Autowired
    DomainService domainService;

    @Autowired
    SkillService skillService;

    @Autowired
    ExpertiseService expertiseService;

    @Autowired
    CompanyService companyService;

    @Autowired
    TitleService titleService;

    @Autowired
    JobService jobService;

    @ModelAttribute("domains")
    public List<DomainDTO> getDomains() {
        return domainService.findAll().stream().map(item -> {
            DomainDTO dto = new DomainDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("skills")
    public List<SkillDTO> getSkills() {
        return skillService.findAll().stream().map(item -> {
            SkillDTO dto = new SkillDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("expertises")
    public List<ExpertiseDTO> getExpertises() {
        return expertiseService.findAll().stream().map(item -> {
            ExpertiseDTO dto = new ExpertiseDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("companies")
    public List<CompanyDTO> getCompanies() {
        return companyService.findAll().stream().map(item -> {
            CompanyDTO dto = new CompanyDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("titles")
    public List<TitleDTO> getTitles() {
        return titleService.findAll().stream().map(item -> {
            TitleDTO dto = new TitleDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("jobCount")
    public long getJobCount() {
        return jobService.count();
    }

    @ModelAttribute("cities")
    public java.util.List<com.quangtrung.HireAI.enums.City> getCities() {
        return java.util.Arrays.stream(City.values())
                .sorted(java.util.Comparator.comparing(com.quangtrung.HireAI.enums.City::getDisplayName))
                .toList();
    }

    @ModelAttribute("featuredSkills")
    public List<SkillDTO> getFeaturedSkills() {
        List<SkillDTO> allSkills = skillService.findAll().stream().map(item -> {
            SkillDTO dto = new SkillDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
        return allSkills.stream().limit(8).toList();
    }

    @ModelAttribute("topHiringCompanies")
    public List<CompanyWithSkillsDTO> getTopHiringCompanies() {
        return companyService.getTopHiringCompanies();
    }

    @RequestMapping("/")
    public String showHome(Model model) {
        return "client/layout/Home";
    }
}
