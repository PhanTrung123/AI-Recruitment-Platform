package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.*;
import com.quangtrung.HireAI.entity.*;
import com.quangtrung.HireAI.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SearchJobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private SkillService skillService;

    @Autowired
    ExpertiseService expertiseService;

    @Autowired
    TitleService titleService;


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


    @GetMapping("/search/domain/{domain_id}")
    public String searchJobDomain(@PathVariable("domain_id") Long domainId, Model model) {
        List<Job> jobs = jobService.findByJobDomain(domainId);

        Domain domain = domainService.findById(domainId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", domain != null ? domain.getDomainName() : "");
        model.addAttribute("searchPrefix", "lĩnh vực ");
        return "client/jobs/SearchJob";
    }

    @GetMapping("/company/{id}")
    public String searchByCompany(@PathVariable("id") Long companyId, Model model) {
        List<Job> jobs = jobService.findByCompanyId(companyId);
        Company company = companyService.findById(companyId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", company != null ? company.getName() : "");
        model.addAttribute("searchPrefix", "tại công ty ");

        return "client/jobs/SearchJob";
    }

    @GetMapping("/search/skill/{skill_id}")
    public String searchJobSkill(@PathVariable("skill_id") Long skillId, Model model) {
        List<Job> jobs = jobService.findBySkill(skillId);

        Skill skill = skillService.findById(skillId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", skill != null ? skill.getSkillName() : "");
        model.addAttribute("searchPrefix", "kỹ năng ");
        return "client/jobs/SearchJob";
    }

    @GetMapping("/search/expertise/{expertisies_id}")
    public String searchExpertise(@PathVariable("expertisies_id") Long expertisiesId, Model model) {
        List<Job> jobs = jobService.findByExpertise(expertisiesId);

        Expertise expertise = expertiseService.findById(expertisiesId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", expertise != null ? expertise.getExpertiseName() : "");
        model.addAttribute("searchPrefix", "chuyên môn");
        return "client/jobs/SearchJob";

    }

    @GetMapping("/search/title/{title_id}")
    public String searchJobTitle(@PathVariable("title_id") Long titleId, Model model) {
        List<Job> jobs = jobService.findByTitle(titleId);

        Title title = titleService.findById(titleId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", title != null ? title.getTitleName() : "");
        model.addAttribute("searchPrefix", "vị trí ");
        return "client/jobs/SearchJob";
    }
}
