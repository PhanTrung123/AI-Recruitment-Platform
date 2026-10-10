package com.quangtrung.HireAI.controller;


import com.quangtrung.HireAI.dto.ExpertiseDTO;
import com.quangtrung.HireAI.entity.Expertise;
import com.quangtrung.HireAI.service.ExpertiseService;
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
@RequestMapping("/admin/expertises")
public class ExpertisesController {
    @Autowired
    ExpertiseService expertiseService;

    @GetMapping("")
    public String showExpertises(Model model, @RequestParam(name = "expertiseName", required = false) String expertiseName,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        model.addAttribute("activeMenu", "expertises");

        Pageable pageable = PageRequest.of(page, size);
        Page<Expertise> skillPage;

        if (StringUtils.hasText(expertiseName)) {
            skillPage = expertiseService.findByExpertiseNameContaining(expertiseName, pageable);
        } else {
            skillPage = expertiseService.findPaginated(pageable);
        }

        model.addAttribute("page", skillPage);
        return "admin/expertises/Expertises";
    }

    @GetMapping("/add")
    public String addExpertise(Model model) {
        model.addAttribute("expertise", new ExpertiseDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "expertises");
        return "admin/expertises/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("expertise") ExpertiseDTO dto, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            if (dto.getExpertisies_id() == null) {
                model.addAttribute("isAdd", true);
            } else {
                model.addAttribute("isEdit", true);
            }
            model.addAttribute("activeMenu", "expertises");
            return new ModelAndView("admin/expertises/Modal", model);
        }
        Expertise entity = new Expertise();
        BeanUtils.copyProperties(dto, entity);
        expertiseService.save(entity);
        return new ModelAndView("redirect:/admin/expertises", model);
    }

    @GetMapping("/edit/{expertisies_id}")
    public ModelAndView edit(ModelMap model, @PathVariable("expertisies_id") Long id) {
        Optional<Expertise> opt = expertiseService.findById(id);
        ExpertiseDTO dto = new ExpertiseDTO();

        if (opt.isPresent()) {
            Expertise entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("expertise", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "expertises");
            return new ModelAndView("admin/expertises/Modal", model);
        }
        model.addAttribute("message", "Expertise is not existed");
        return new ModelAndView("forward:/admin/expertises", model);
    }

    @GetMapping("/delete/{expertisies_id}")
    public ModelAndView delete(ModelMap model, @PathVariable("expertisies_id") Long id) {
        expertiseService.deleteById(id);
        model.addAttribute("message", "Expertise is deleted!");
        return new ModelAndView("redirect:/admin/expertises", model);
    }
}
