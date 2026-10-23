package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.SessionUserDTO;
import com.quangtrung.HireAI.dto.SkillDTO;
import com.quangtrung.HireAI.entity.CV;
import com.quangtrung.HireAI.entity.Job;
import com.quangtrung.HireAI.entity.MyJob;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class ApplyJobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private CVService cvService;

    @Autowired
    private MyJobService myJobService;

    @Autowired
    private SkillService skillService;

    @ModelAttribute("skills")
    public List<SkillDTO> getSkills() {
        return skillService.findAll().stream().map(item -> {
            SkillDTO dto = new SkillDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @GetMapping("/apply-job/{job_id}")
    public String showApplyJob(@PathVariable("job_id") Long jobId, Model model, HttpSession session) {
        Job job = jobService.findById(jobId).orElse(null);
        model.addAttribute("job", job);

        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findById(sessionUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<CV> latestCV = cvService.getLatestCVByUser(user);
            model.addAttribute("userCv", latestCV.orElse(null));

            // Check if user already applied to this job
            Optional<MyJob> existingApplication = myJobService.findByUserAndJobId(user, jobId);
            if (existingApplication.isPresent()) {
                model.addAttribute("alreadyApplied", true);
                model.addAttribute("appliedDate", existingApplication.get().getAppliedAt());
            } else {
                model.addAttribute("alreadyApplied", false);
            }
        }

        return "client/applyJob/ApplyJob";
    }

    @PostMapping("/apply-job/{job_id}")
    public String submitApplyJob(
            @PathVariable("job_id") Long jobId,
            @RequestParam(value = "coverLetter", required = false) String coverLetter,
            @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
            @RequestParam(value = "existingCvId", required = false) Long existingCvId,
            @RequestParam(value = "cv_option", required = false) String cvOption,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        User currentUser = userService.findById(sessionUser.getId()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        CV targetCv = null;
        if ("upload".equals(cvOption)) {

            if (cvFile == null || cvFile.isEmpty()) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage", "Vui lòng upload CV của bạn!");
                return "redirect:/apply-job/" + jobId;
            }
            String filename = cvFile.getOriginalFilename();
            if (filename == null ||
                    !(filename.toLowerCase().endsWith(".pdf")
                            || filename.toLowerCase().endsWith(".docx"))) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage", "Chỉ chấp nhận file PDF hoặc DOCX!");
                return "redirect:/apply-job/" + jobId;
            }
            String fileUrl = fileService.saveFile(cvFile);
            CV newCv = new CV();
            newCv.setUser(currentUser);
            newCv.setFileUrl(fileUrl);
            newCv.setCreatedAt(LocalDateTime.now());
            targetCv = cvService.save(newCv);
        } else if ("existing".equals(cvOption)) {
            if (existingCvId == null) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage", "Vui lòng chọn CV có sẵn!");
                return "redirect:/apply-job/" + jobId;
            }
            targetCv = cvService.findById(existingCvId).orElse(null);
            if (targetCv == null || !targetCv.getUser().getId().equals(currentUser.getId())) {
                redirectAttributes.addFlashAttribute(
                        "errorMessage", "CV không hợp lệ!");
                return "redirect:/apply-job/" + jobId;
            }
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Vui lòng chọn phương thức nộp CV!");
            return "redirect:/apply-job/" + jobId;
        }
        // Lưu thông tin vào bảng My_jobs
        Job job = jobService.findById(jobId).orElse(null);
        if (job == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy công việc!");
            return "redirect:/apply-job/" + jobId;
        }

        // Check if user already applied to this job
        Optional<MyJob> existingApplication = myJobService.findByUserAndJobId(currentUser, jobId);
        if (existingApplication.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn đã ứng tuyển công việc này rồi!");
            return "redirect:/apply-job/" + jobId;
        }

        MyJob myJob = new MyJob();
        myJob.setJob(job);
        myJob.setCv(targetCv);
        myJob.setCoverLetter(coverLetter);
        myJob.setStatus("PENDING");
        myJob.setAppliedAt(LocalDateTime.now());

        myJobService.save(myJob);

        redirectAttributes.addFlashAttribute("successMessage", "Nộp hồ sơ ứng tuyển thành công!");
        return "redirect:/apply-success?job_id=" + jobId;
    }

    @GetMapping("/apply-success")
    public String showApplySuccess(@RequestParam(value = "job_id", required = false) Long jobId, Model model) {
        if (jobId != null) {
            Job job = jobService.findById(jobId).orElse(null);
            model.addAttribute("job", job);

            if (job != null && job.getCity() != null) {
                List<Job> relatedJobs = jobService.findByCity(job.getCity());
                model.addAttribute("relatedJobs", relatedJobs.stream().limit(4).toList());
            }
        }
        List<SkillDTO> allSkills = skillService.findAll().stream().map(item -> {
            SkillDTO dto = new SkillDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
        model.addAttribute("featuredSkills", allSkills.stream().limit(7).toList());

        return "client/applyJob/ApplySuccess";
    }

}
