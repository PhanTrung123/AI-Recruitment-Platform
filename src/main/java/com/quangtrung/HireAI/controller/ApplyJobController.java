package com.quangtrung.HireAI.controller;

import com.cloudinary.Cloudinary;
import com.quangtrung.HireAI.entity.Job;
import com.quangtrung.HireAI.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ApplyJobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/apply-job/{job_id}")
    public String showApplyJob(@PathVariable("job_id") Long jobId, Model model) {
        Job job = jobService.findById(jobId).orElse(null);
        model.addAttribute("job", job);
        return "client/applyJob/ApplyJob";
    }

}
