package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.*;
import com.quangtrung.HireAI.entity.*;
import com.quangtrung.HireAI.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/jobs")
public class JobsController {

    @Autowired
    CompanyService companyService;

    @Autowired
    ExpertiseService expertiseService;

    @Autowired
    JobService jobService;

    @Autowired
    DomainService domainService;

    @Autowired
    SkillService skillService;

    @Autowired
    SkillJobService skillJobService;

    @Autowired
    JobDomainService jobDomainService;

    @Autowired
    TitleService titleService;

    @ModelAttribute("companies")
    public List<CompanyDTO> getCompany() {
        return companyService.findAll().stream().map(item -> {
            CompanyDTO dto = new CompanyDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("expertises")
    public List<ExpertiseDTO> getExpertise() {
        return expertiseService.findAll().stream().map(item -> {
            ExpertiseDTO dto = new ExpertiseDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("domains")
    public List<DomainDTO> getDomain() {
        return domainService.findAll().stream().map(item -> {
            DomainDTO dto = new DomainDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("skills")
    public List<SkillDTO> getSkill() {
        return skillService.findAll().stream().map(item -> {
            SkillDTO dto = new SkillDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @ModelAttribute("titles")
    public List<TitleDTO> getTitle() {
        return titleService.findAll().stream().map(item -> {
            TitleDTO dto = new TitleDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }


    @GetMapping("")
    public String showJobs(Model model, @RequestParam(name = "name", required = false) String name,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "5") int size) {
        model.addAttribute("activeMenu", "jobs");

        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobPage;

        if (StringUtils.hasText(name)) {
            jobPage = jobService.findByJobNameContaining(name, pageable);
        } else {
            jobPage = jobService.findPaginated(pageable);
        }

        model.addAttribute("page", jobPage);
        return "admin/jobs/Jobs";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("job", new JobDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "jobs");
        return "admin/jobs/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@ModelAttribute("job") JobDTO dto, ModelMap model) {
        Job entity;
        // Có ID thì cập nhật Job cũ
        if (dto.getJob_id() != null) {
            entity = jobService.findById(dto.getJob_id())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy Job ID: " + dto.getJob_id()
                    ));
        } else {
            // Không có ID thì tạo Job mới
            entity = new Job();
        }
        BeanUtils.copyProperties(dto, entity);

        Company company = new Company();
        company.setCompany_id(dto.getCompany_id());
        entity.setCompany(company);

        Expertise expertise = new Expertise();
        expertise.setExpertisies_id(dto.getExpertisies_id());
        entity.setExpertise(expertise);

        Title title = new Title();
        title.setTitle_id(dto.getTitle_id());
        entity.setTitle(title);

        jobService.save(entity);
        for (Long domainId : dto.getDomain_id()) {
            JobDomain jobDomain = new JobDomain();
            jobDomain.setJob(entity);
            jobDomain.setDomain(domainService.findById(domainId).orElse(null));
            jobDomainService.save(jobDomain);
        }

        for (Long skillId : dto.getSkill_id()) {
            SkillJob skillJob = new SkillJob();
            skillJob.setJob(entity);
            skillJob.setSkill(skillService.findById(skillId).orElse(null));
            skillJobService.save(skillJob);

        }


        return new ModelAndView("redirect:/admin/jobs", model);
    }

    @GetMapping("/edit/{job_id}")
    @Transactional(readOnly = true)
    public ModelAndView edit(ModelMap model, @PathVariable("job_id") Long id) {
        Optional<Job> opt = jobService.findByIdWithRelations(id);
        JobDTO dto = new JobDTO();

        if (opt.isPresent()) {
            Job entity = opt.get();
            BeanUtils.copyProperties(entity, dto);

            // Set company_id from company relationship
            if (entity.getCompany() != null) {
                dto.setCompany_id(entity.getCompany().getCompany_id());
            }

            // Set expertisies_id from expertise relationship
            if (entity.getExpertise() != null) {
                dto.setExpertisies_id(entity.getExpertise().getExpertisies_id());
            }

            // Set title_id from title relationship
            if (entity.getTitle() != null) {
                dto.setTitle_id(entity.getTitle().getTitle_id());
            }

            // Extract skill IDs from skillJobs relationship
            if (entity.getSkillJobs() != null && !entity.getSkillJobs().isEmpty()) {
                List<Long> skillIds = entity.getSkillJobs().stream()
                        .map(skillJob -> skillJob.getSkill().getSkill_id())
                        .toList();
                dto.setSkill_id(skillIds);
            }

            // Extract domain IDs from jobDomains relationship
            if (entity.getJobDomains() != null && !entity.getJobDomains().isEmpty()) {
                List<Long> domainIds = entity.getJobDomains().stream()
                        .map(jobDomain -> jobDomain.getDomain().getDomain_id())
                        .toList();
                dto.setDomain_id(domainIds);
            }

            dto.setIsEdit(true);
            model.addAttribute("job", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "jobs");
            return new ModelAndView("admin/jobs/Modal", model);
        }
        model.addAttribute("message", "Job is not existed!");
        return new ModelAndView("forward:/admin/jobs", model);
    }

    @GetMapping("/delete/{job_id}")
    public ModelAndView delete(ModelMap model, @PathVariable("job_id") Long id) {
        jobService.deleteById(id);
        model.addAttribute("message", "Job is deleted!");
        return new ModelAndView("redirect:/admin/jobs", model);
    }
}
