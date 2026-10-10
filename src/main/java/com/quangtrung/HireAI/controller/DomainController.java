package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.DomainDTO;
import com.quangtrung.HireAI.entity.Domain;
import com.quangtrung.HireAI.service.DomainService;
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
@RequestMapping("/admin/domains")
public class DomainController {

    @Autowired
    DomainService domainService;

    @RequestMapping("")
    public String showDomains(Model model, @RequestParam(name = "name", required = false) String name,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "5") int size) {

        model.addAttribute("activeMenu", "domains");

        Pageable pageable = PageRequest.of(page, size);
        Page<Domain> domainPage;

        if (StringUtils.hasText(name)) {
            domainPage = domainService.findByDomainNameContaining(name, pageable);
        } else {
            domainPage = domainService.findPaginated(pageable);
        }

        model.addAttribute("page", domainPage);
        return "admin/domains/Domains";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("domain", new DomainDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "domains");
        return "admin/domains/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("domain") DomainDTO dto, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            if (dto.getDomain_id() == null) {
                model.addAttribute("isAdd", true);
            } else {
                model.addAttribute("isEdit", true);
            }
            model.addAttribute("activeMenu", "domains");
            return new ModelAndView("admin/domains/Modal", model);
        }
        Domain entity = new Domain();
        BeanUtils.copyProperties(dto, entity);
        domainService.save(entity);
        return new ModelAndView("redirect:/admin/domains", model);
    }

    @GetMapping("/edit/{domain_id}")
    public ModelAndView edit(ModelMap model, @PathVariable("domain_id") Long id) {
        Optional<Domain> opt = domainService.findById(id);
        DomainDTO dto = new DomainDTO();

        if (opt.isPresent()) {
            Domain entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("domain", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "domains");
            return new ModelAndView("admin/domains/Modal", model);
        }
        model.addAttribute("message", "Domain is not existed!");
        return new ModelAndView("forward:/admin/domains", model);
    }


    @GetMapping("/delete/{domain_id}")
    public ModelAndView delete(ModelMap model, @PathVariable("domain_id") Long id) {
        domainService.deleteById(id);
        model.addAttribute("message", "Domain is deleted!");
        return new ModelAndView("redirect:/admin/domains", model);
    }
}