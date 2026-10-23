package com.quangtrung.HireAI.controller.recruiter;

import com.quangtrung.HireAI.dto.*;
import com.quangtrung.HireAI.entity.*;
import com.quangtrung.HireAI.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("employer/dashboard")
public class ProfileRecruiterController {

    @Autowired
    CompanyService companyService;

    @Autowired
    private JobService jobService;

    @Autowired
    DomainService domainService;

    @Autowired
    ExpertiseService expertiseService;

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

    @GetMapping({"", "/{section}"})
    public String dashboard(@PathVariable(required = false) String section, Model model, HttpSession session, @RequestParam(required = false) Boolean create) {
        String activeItem = resolveActiveItem(section);
        model.addAttribute("activeItem", activeItem);
        model.addAttribute("pageTitle", resolvePageTitle(activeItem));

        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        if ("job-openings".equals(activeItem)) {
            Long userId = sessionUser.getId();
            List<JobDTO> jobs = jobService.getJobsByUserId(userId);
            model.addAttribute("jobs", jobs);
            model.addAttribute("newJob", new JobDTO());
            if (Boolean.TRUE.equals(create)) {
                model.addAttribute("showModal", true);
            }
            return "recruiter/dashboard/JobOpening";
        }
        return "recruiter/dashboard/EmpDashboard";
    }

    @PostMapping("/job-openings/save")
    public String addOrUpdate(@ModelAttribute("newJob") JobDTO dto, HttpSession session, RedirectAttributes redirectAttributes) {

        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        Job entity;
        boolean isUpdate = dto.getJob_id() != null;

        if (isUpdate) {
            entity = jobService.findById(dto.getJob_id()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Job ID: " + dto.getJob_id()));

            jobDomainService.deleteByJobId(dto.getJob_id());
            skillJobService.deleteByJobId(entity.getJob_id());
        } else {
            entity = new Job();
        }
        BeanUtils.copyProperties(dto, entity, "job_id", "domain_id", "skill_id", "company_id");

        Company company = companyService.findByUserId(sessionUser.getId());
        if (company == null) {
            throw new RuntimeException("Tài khoản này chưa có công ty hoặc chưa được duyệt!");
        }
        entity.setCompany(company);

        Expertise expertise = expertiseService.findById(dto.getExpertisies_id())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Chuyên môn"));
        entity.setExpertise(expertise);

        Title title = titleService.findById(dto.getTitle_id())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Vị trí"));
        entity.setTitle(title);

        jobService.save(entity);

        if (dto.getDomain_id() != null && !dto.getDomain_id().isEmpty()) {
            for (Long domainId : dto.getDomain_id()) {
                JobDomain jobDomain = new JobDomain();
                jobDomain.setJob(entity);
                Domain domain = domainService.findById(domainId).orElse(null);
                jobDomain.setDomain(domain);
                jobDomainService.save(jobDomain);
            }
        }

        if (dto.getSkill_id() != null && !dto.getSkill_id().isEmpty()) {
            for (Long skillId : dto.getSkill_id()) {
                SkillJob skillJob = new SkillJob();
                skillJob.setJob(entity);
                Skill skill = skillService.findById(skillId).orElse(null);
                skillJob.setSkill(skill);
                skillJobService.save(skillJob);
            }
        }
        String message = isUpdate ? "Cập nhật công việc thành công!" : "Thêm mới công việc thành công!";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/employer/dashboard/job-openings";
    }

    @GetMapping("/job-opening/edit/{job_id}")
    public ModelAndView editJob(ModelMap model, @PathVariable("job_id") Long id, HttpSession session) {
        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
        if (sessionUser == null) {
            return new ModelAndView("redirect:/login");
        }
        Job entity = jobService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy công việc với ID: " + id));
        if (!entity.getCompany().getUser().getId().equals(sessionUser.getId())) {
            return new ModelAndView("redirect:/recruiter/dashboard/job-openings");
        }
        JobDTO dto = new JobDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setIsEdit(true);
        if (entity.getCompany() != null) {
            dto.setCompany_id(entity.getCompany().getCompany_id());
        }
        if (entity.getExpertise() != null) {
            dto.setExpertisies_id(entity.getExpertise().getExpertisies_id());
        }
        if (entity.getTitle() != null) {
            dto.setTitle_id(entity.getTitle().getTitle_id());
        }
        if (entity.getJobDomains() != null) {
            List<Long> domainIds = entity.getJobDomains().stream()
                    .map(jd -> jd.getDomain().getDomain_id())
                    .toList();
            dto.setDomain_id(domainIds);
        }
        if (entity.getSkillJobs() != null) {
            List<Long> skillIds = entity.getSkillJobs().stream()
                    .map(sj -> sj.getSkill().getSkill_id())
                    .toList();
            dto.setSkill_id(skillIds);
        }
        model.addAttribute("newJob", dto);
        model.addAttribute("showModal", true);
        model.addAttribute("activeItem", "job-openings");
        model.addAttribute("pageTitle", "Tin tuyển dụng");
        List<JobDTO> jobs = jobService.getJobsByUserId(sessionUser.getId());
        model.addAttribute("jobs", jobs);
        return new ModelAndView("recruiter/dashboard/JobOpening", model);
    }

    @PostMapping("/job-opening/delete/{id}")
    public String deleteJob(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        jobService.deleteJobCompletely(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa tin tuyển dụng thành công!");
        return "redirect:/employer/dashboard/job-openings";
    }


    private String resolveActiveItem(String section) {
        if (section == null || section.isBlank() || "overview".equals(section)) {
            return "overview";
        }

        switch (section) {
            case "applicants":
                return "applicants";
            case "company-profile":
                return "company-profile";
            case "job-openings":
                return "job-openings";
            case "invitations":
                return "invitations";
            case "subscriptions":
                return "subscriptions";
            case "notifications":
                return "notifications";
            case "settings":
                return "settings";
            default:
                return "overview";
        }
    }

    private String resolvePageTitle(String activeItem) {
        switch (activeItem) {
            case "applicants":
                return "Hồ sơ ứng tuyển";
            case "company-profile":
                return "Thông tin công ty";
            case "job-openings":
                return "Tin tuyển dụng";
            case "invitations":
                return "Lời mời đã gửi";
            case "subscriptions":
                return "Gói tuyển dụng";
            case "notifications":
                return "Thông báo";
            case "settings":
                return "Cài đặt";
            case "overview":
            default:
                return "Bảng điều khiển";
        }
    }
}
