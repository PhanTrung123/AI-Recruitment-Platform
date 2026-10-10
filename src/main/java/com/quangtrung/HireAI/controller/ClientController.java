package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.CompanyDTO;
import com.quangtrung.HireAI.dto.DomainDTO;
import com.quangtrung.HireAI.dto.ExpertiseDTO;
import com.quangtrung.HireAI.dto.SkillDTO;
import com.quangtrung.HireAI.service.CompanyService;
import com.quangtrung.HireAI.service.DomainService;
import com.quangtrung.HireAI.service.ExpertiseService;
import com.quangtrung.HireAI.service.SkillService;
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

//    @ModelAttribute("cities")
//    public City[] getCities() {
//        return City.values();
//    }

    @RequestMapping("/")
    public String showHome(Model model) {
        return "client/layout/Home";
    }
}
