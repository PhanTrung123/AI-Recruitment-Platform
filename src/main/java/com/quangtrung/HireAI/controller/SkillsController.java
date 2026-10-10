package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.SkillDTO;
import com.quangtrung.HireAI.entity.Skill;
import com.quangtrung.HireAI.service.SkillService;
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
@RequestMapping("/admin/skills")
public class SkillsController {

    @Autowired
    SkillService skillService;

    @GetMapping("")
    public String showSkills(Model model, @RequestParam(name = "name", required = false) String name,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        model.addAttribute("activeMenu", "skills");

        Pageable pageable = PageRequest.of(page, size);
        Page<Skill> skillPage;

        if (StringUtils.hasText(name)) {
            skillPage = skillService.findBySkillNameContaining(name, pageable);
        } else {
            skillPage = skillService.findPaginated(pageable);
        }

        model.addAttribute("page", skillPage);
        return "admin/skills/Skills";
    }

    @GetMapping("/add")
    public String addSkills(Model model) {
        model.addAttribute("skill", new SkillDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "skills");
        return "admin/skills/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("skill") SkillDTO dto, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            if (dto.getSkill_id() == null) {
                model.addAttribute("isAdd", true);
            } else {
                model.addAttribute("isEdit", true);
            }
            model.addAttribute("activeMenu", "skills");
            return new ModelAndView("admin/skills/Modal", model);
        }
        Skill entity = new Skill();
        BeanUtils.copyProperties(dto, entity);
        skillService.save(entity);
        return new ModelAndView("redirect:/admin/skills", model);
    }

    @GetMapping("/edit/{skill_id}")
    public ModelAndView edit(ModelMap model, @PathVariable("skill_id") Long id) {
        Optional<Skill> opt = skillService.findById(id);
        SkillDTO dto = new SkillDTO();

        if (opt.isPresent()) {
            Skill entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("skill", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "skills");
            return new ModelAndView("admin/skills/Modal", model);
        }
        model.addAttribute("message", "Skill is not existed");
        return new ModelAndView("forward:/admin/skills", model);
    }

    @GetMapping("/delete/{skill_id}")
    public ModelAndView delete(ModelMap model, @PathVariable("skill_id") Long id) {
        skillService.deleteById(id);
        model.addAttribute("message", "Skill is deleted!");
        return new ModelAndView("redirect:/admin/skills", model);
    }
}
