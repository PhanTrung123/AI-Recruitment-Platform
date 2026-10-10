package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.CategoryDTO;
import com.quangtrung.HireAI.entity.Category;
import com.quangtrung.HireAI.service.CategoryService;
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
@RequestMapping("/admin/categories")
public class CategoriesController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("")
    public String showCategory(Model model, @RequestParam(name = "name", required = false) String name,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size) {

        model.addAttribute("activeMenu", "categories");

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage;

        if (StringUtils.hasText(name)) {
            categoryPage = categoryService.findByCategoryNameContaining(name, pageable);
        } else {
            categoryPage = categoryService.findPaginated(pageable);
        }

        model.addAttribute("page", categoryPage);
        return "admin/categories/Categories";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("category", new CategoryDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "categories");
        return "admin/categories/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("category") CategoryDTO dto, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            if (dto.getCategory_id() == null) {
                model.addAttribute("isAdd", true);
            } else {
                model.addAttribute("isEdit", true);
            }
            model.addAttribute("activeMenu", "categories");
            return new ModelAndView("admin/categories/Modal", model);
        }
        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);
        categoryService.save(entity);
        return new ModelAndView("redirect:/admin/categories", model);
    }

    @GetMapping("/edit/{category_id}")
    public ModelAndView edit(ModelMap model, @PathVariable("category_id") Long id) {
        Optional<Category> opt = categoryService.findById(id);
        CategoryDTO dto = new CategoryDTO();

        if (opt.isPresent()) {
            Category entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("category", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "categories");
            return new ModelAndView("admin/categories/Modal", model);
        }
        model.addAttribute("message", "Category is not existed!");
        return new ModelAndView("forward:/admin/categories", model);
    }


    @GetMapping("/delete/{category_id}")
    public ModelAndView delete(ModelMap model, @PathVariable("category_id") Long id) {
        categoryService.deleteById(id);
        model.addAttribute("message", "Category is deleted!");
        return new ModelAndView("redirect:/admin/categories", model);
    }

}
