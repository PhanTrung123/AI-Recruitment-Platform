package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.TitleDTO;
import com.quangtrung.HireAI.entity.Title;
import com.quangtrung.HireAI.service.TitleService;
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
@RequestMapping("/admin/titles")
public class TitlesController {
    @Autowired
    TitleService titleService;

    @GetMapping("")
    public String showTitles(Model model, @RequestParam(name = "titleName", required = false) String titleName,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "5") int size) {
        model.addAttribute("activeMenu", "titles");

        Pageable pageable = PageRequest.of(page, size);
        Page<Title> titlePage;

        if (StringUtils.hasText(titleName)) {
            titlePage = titleService.findByTitleNameContaining(titleName, pageable);
        } else {
            titlePage = titleService.findPaginated(pageable);
        }
        model.addAttribute("page", titlePage);
        return "admin/titles/Titles";
    }

    @GetMapping("/add")
    public String addTitle(Model model) {
        model.addAttribute("title", new TitleDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "titles");
        return "admin/titles/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("title") TitleDTO dto, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            if (dto.getTitle_id() == null) {
                model.addAttribute("isAdd", true);
            } else {
                model.addAttribute("isEdit", true);
            }
            model.addAttribute("activeMenu", "titles");
            return new ModelAndView("admin/titles/Modal", model);
        }
        Title entity = new Title();
        BeanUtils.copyProperties(dto, entity);
        titleService.save(entity);
        return new ModelAndView("redirect:/admin/titles", model);
    }

    @GetMapping("/edit/{title_id}")
    public ModelAndView edit(ModelMap model, @PathVariable("title_id") Long id) {
        Optional<Title> opt = titleService.findById(id);
        TitleDTO dto = new TitleDTO();

        if (opt.isPresent()) {
            Title entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("title", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "titles");
            return new ModelAndView("admin/titles/Modal", model);
        }
        model.addAttribute("message", "Title is not existed");
        return new ModelAndView("forward:/admin/titles", model);
    }

    @GetMapping("/delete/{title_id}")
    public ModelAndView delete(ModelMap model, @PathVariable("title_id") Long id) {
        titleService.deleteById(id);
        model.addAttribute("message", "Title is deleted");
        return new ModelAndView("redirect:/admin/titles", model);
    }
}


