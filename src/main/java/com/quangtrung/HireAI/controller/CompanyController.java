package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.CompanyDTO;
import com.quangtrung.HireAI.entity.Company;
import com.quangtrung.HireAI.service.CompanyService;
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

import java.util.Optional;

@Controller
@RequestMapping("/admin/companies")
public class CompanyController {

    @Autowired
    CompanyService companyService;


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
        return "admin/companies/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("company") CompanyDTO dto, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("company", dto);
            model.addAttribute("isAdd", dto.getCompany_id() == null);
            model.addAttribute("isEdit", dto.getIsEdit());
            model.addAttribute("activeMenu", "companies");
            return new ModelAndView("admin/companies/Modal", model);
        }
        Company entity = new Company();
        BeanUtils.copyProperties(dto, entity);
        companyService.save(entity);
        return new ModelAndView("redirect:/admin/companies", model);
    }

    @GetMapping("/edit/{company_id}")
    public ModelAndView edit(ModelMap model, @PathVariable("company_id") Long id) {
        Optional<Company> opt = companyService.findById(id);
        CompanyDTO dto = new CompanyDTO();

        if (opt.isPresent()) {
            Company entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("company", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "companies");
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
}