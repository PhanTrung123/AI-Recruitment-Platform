package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.entity.Company;
import com.quangtrung.HireAI.entity.Domain;
import com.quangtrung.HireAI.entity.Job;
import com.quangtrung.HireAI.service.CompanyService;
import com.quangtrung.HireAI.service.DomainService;
import com.quangtrung.HireAI.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/search/domain/{domain_id}")
    public String searchJobDomain(@PathVariable("domain_id") Long domainId, Model model) {
        List<Job> jobs = jobService.findByJobDomain(domainId);

        Domain domain = domainService.findById(domainId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("domainName", domain != null ? domain.getDomainName() : "");
        model.addAttribute("searchPrefix", "lĩnh vực ");
        return "client/jobs/SearchJob";
    }

    @GetMapping("/company/{id}")
    public String searchByCompany(@PathVariable("id") Long companyId, Model model) {
        List<Job> jobs = jobService.findByCompanyId(companyId);
        Company company = companyService.findById(companyId).orElse(null);

        model.addAttribute("jobs", jobs);
        model.addAttribute("searchKeyword", company != null ? company.getName() : "");
        model.addAttribute("searchPrefix", "tại công ty ");

        return "client/jobs/SearchJob";
    }
}
