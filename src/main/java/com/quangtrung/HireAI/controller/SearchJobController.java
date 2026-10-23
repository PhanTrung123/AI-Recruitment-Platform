package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.*;
import com.quangtrung.HireAI.entity.*;
import com.quangtrung.HireAI.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private MyJobService myJobService;

    @Autowired
    private UserService userService;


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

    private Map<Long, java.time.LocalDateTime> getAppliedJobStatus(List<Job> jobs, HttpSession session) {
        Map<Long, java.time.LocalDateTime> appliedStatus = new HashMap<>();
        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
        if (sessionUser != null) {
            User user = userService.findById(sessionUser.getId()).orElse(null);
            if (user != null) {
                for (Job job : jobs) {
                    myJobService.findByUserAndJobId(user, job.getJob_id()).ifPresent(myJob -> {
                        appliedStatus.put(job.getJob_id(), myJob.getAppliedAt());
                    });
                }
            }
        }
        return appliedStatus;
    }


    @GetMapping("/search/domain/{domain_id}")
    public String searchJobDomain(@PathVariable("domain_id") Long domainId, Model model, HttpSession session) {
        List<Job> jobs = jobService.findByJobDomain(domainId);

        Domain domain = domainService.findById(domainId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", domain != null ? domain.getDomainName() : "");
        model.addAttribute("searchPrefix", "lĩnh vực ");
        model.addAttribute("appliedStatus", getAppliedJobStatus(jobs, session));
        return "client/jobs/SearchJob";
    }

    @GetMapping("/company/{id}")
    public String searchByCompany(@PathVariable("id") Long companyId, Model model, HttpSession session) {
        List<Job> jobs = jobService.findByCompanyId(companyId);
        Company company = companyService.findById(companyId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", company != null ? company.getName() : "");
        model.addAttribute("searchPrefix", "tại công ty ");
        model.addAttribute("appliedStatus", getAppliedJobStatus(jobs, session));

        return "client/jobs/SearchJob";
    }

    @GetMapping("/search/skill/{skill_id}")
    public String searchJobSkill(@PathVariable("skill_id") Long skillId, Model model, HttpSession session) {
        List<Job> jobs = jobService.findBySkill(skillId);

        Skill skill = skillService.findById(skillId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", skill != null ? skill.getSkillName() : "");
        model.addAttribute("searchPrefix", "kỹ năng ");
        model.addAttribute("appliedStatus", getAppliedJobStatus(jobs, session));
        return "client/jobs/SearchJob";
    }

    @GetMapping("/search/expertise/{expertisies_id}")
    public String searchExpertise(@PathVariable("expertisies_id") Long expertisiesId, Model model, HttpSession session) {
        List<Job> jobs = jobService.findByExpertise(expertisiesId);

        Expertise expertise = expertiseService.findById(expertisiesId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", expertise != null ? expertise.getExpertiseName() : "");
        model.addAttribute("searchPrefix", "chuyên môn");
        model.addAttribute("appliedStatus", getAppliedJobStatus(jobs, session));
        return "client/jobs/SearchJob";

    }

    @GetMapping("/search/title/{title_id}")
    public String searchJobTitle(@PathVariable("title_id") Long titleId, Model model, HttpSession session) {
        List<Job> jobs = jobService.findByTitle(titleId);

        Title title = titleService.findById(titleId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", title != null ? title.getTitleName() : "");
        model.addAttribute("searchPrefix", "vị trí ");
        model.addAttribute("appliedStatus", getAppliedJobStatus(jobs, session));
        return "client/jobs/SearchJob";
    }

    @GetMapping("/search/city/{city_id}")
    public String searchJobCity(@PathVariable("city_id") Long cityId,
                                @RequestParam(value = "active_job_id", required = false) Long activeJobId,
                                Model model,
                                HttpSession session) {
        com.quangtrung.HireAI.enums.City[] cities = com.quangtrung.HireAI.enums.City.values();
        if (cityId < 0 || cityId >= cities.length) {
            model.addAttribute("jobs", List.of());
            model.addAttribute("filterName", "");
            model.addAttribute("searchPrefix", "tại ");
            model.addAttribute("activeJobId", activeJobId);
            model.addAttribute("appliedStatus", new HashMap<>());
            return "client/jobs/SearchJob";
        }

        com.quangtrung.HireAI.enums.City city = cities[cityId.intValue()];
        List<Job> jobs = jobService.findByCity(city);

        model.addAttribute("jobs", jobs);
        model.addAttribute("filterName", city.getDisplayName());
        model.addAttribute("searchPrefix", "tại ");
        model.addAttribute("activeJobId", activeJobId);
        model.addAttribute("appliedStatus", getAppliedJobStatus(jobs, session));
        return "client/jobs/SearchJob";
    }
}
