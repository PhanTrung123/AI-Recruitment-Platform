package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.CompanyDTO;
import com.quangtrung.HireAI.entity.Company;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.enums.Role;
import com.quangtrung.HireAI.service.CompanyService;
import com.quangtrung.HireAI.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/companies")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    @GetMapping("")
    public String showCompanies(Model model, @RequestParam(name = "name", required = false) String name,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "5") int size) {
        model.addAttribute("activeMenu", "companies");

        Pageable pageable = PageRequest.of(page, size);
        Page<Company> companyPage;

        if (StringUtils.hasText(name)) {
            companyPage = companyService.findByNameContaining(name, pageable);
        } else {
            companyPage = companyService.findPaginated(pageable);
        }

        model.addAttribute("page", companyPage);
        return "admin/companies/Companies";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("company", new CompanyDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "companies");
        List<User> recruiters = userService.findByRole(Role.RECRUITER);
        model.addAttribute("recruiters", recruiters);
        return "admin/companies/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("company") CompanyDTO dto, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("company", dto);
            model.addAttribute("isAdd", dto.getCompany_id() == null);
            model.addAttribute("isEdit", dto.getIsEdit());
            model.addAttribute("activeMenu", "companies");
            List<User> recruiters = userService.findByRole(Role.RECRUITER);
            model.addAttribute("recruiters", recruiters);
            return new ModelAndView("admin/companies/Modal", model);
        }

        if (dto.getUserId() != null) {
            User user = userService.findById(dto.getUserId()).orElse(null);
            if (user == null) {
                bindingResult.rejectValue("userId", "error.userId", "Không tìm thấy người dùng!");
                List<User> recruiters = userService.findByRole(Role.RECRUITER);
                model.addAttribute("recruiters", recruiters);
                model.addAttribute("company", dto);
                model.addAttribute("activeMenu", "companies");
                return new ModelAndView("admin/companies/Modal", model);
            }
            if (user.getRole() != Role.RECRUITER) {
                bindingResult.rejectValue("userId", "error.userId", "Người dùng phải có vai trò RECRUITER!");
                List<User> recruiters = userService.findByRole(Role.RECRUITER);
                model.addAttribute("recruiters", recruiters);
                model.addAttribute("company", dto);
                model.addAttribute("activeMenu", "companies");
                return new ModelAndView("admin/companies/Modal", model);
            }
        }

        companyService.saveFromDTO(dto);
        return new ModelAndView("redirect:/admin/companies", model);
    }

    @GetMapping("/edit/{company_id}")
    public ModelAndView edit(ModelMap model, @PathVariable("company_id") Long id) {
        Optional<Company> opt = companyService.findById(id);
        CompanyDTO dto = new CompanyDTO();

        if (opt.isPresent()) {
            Company entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            if (entity.getUser() != null) {
                dto.setUserId(entity.getUser().getId());
                dto.setUserName(entity.getUser().getUserName());
                dto.setUserEmail(entity.getUser().getEmail());
            }
            dto.setIsEdit(true);
            model.addAttribute("company", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "companies");
            List<User> recruiters = userService.findByRole(Role.RECRUITER);
            model.addAttribute("recruiters", recruiters);
            return new ModelAndView("admin/companies/Modal", model);
        }
        model.addAttribute("message", "Company is not existed");
        return new ModelAndView("forward:/admin/companies", model);
    }

    @GetMapping("delete/{company_id}")
    public ModelAndView delete(ModelMap model, @PathVariable("company_id") Long id) {
        companyService.deleteById(id);
        model.addAttribute("message", "Company is deleted!");
        return new ModelAndView("redirect:/admin/companies", model);
    }

    @GetMapping("/approve/{company_id}")
    public ModelAndView approve(ModelMap model, @PathVariable("company_id") Long id) {
        Optional<Company> opt = companyService.findById(id);
        if (opt.isPresent()) {
            Company company = opt.get();
            company.setStatus(com.quangtrung.HireAI.enums.CompanyStatus.APPROVED);
            companyService.save(company);
            
            // Upgrade user role to RECRUITER
            if (company.getUser() != null) {
                userService.updateRole(company.getUser().getId(), Role.RECRUITER);
            }
            
            model.addAttribute("message", "Company approved successfully!");
        } else {
            model.addAttribute("message", "Company not found!");
        }
        return new ModelAndView("redirect:/admin/companies", model);
    }

    @GetMapping("/reject/{company_id}")
    public ModelAndView reject(ModelMap model, @PathVariable("company_id") Long id) {
        Optional<Company> opt = companyService.findById(id);
        if (opt.isPresent()) {
            Company company = opt.get();
            company.setStatus(com.quangtrung.HireAI.enums.CompanyStatus.REJECTED);
            companyService.save(company);
            
            model.addAttribute("message", "Company rejected successfully!");
        } else {
            model.addAttribute("message", "Company not found!");
        }
        return new ModelAndView("redirect:/admin/companies", model);
    }
}