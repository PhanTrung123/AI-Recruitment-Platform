package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.BlogDTO;
import com.quangtrung.HireAI.dto.CategoryDTO;
import com.quangtrung.HireAI.entity.Blog;
import com.quangtrung.HireAI.entity.Category;
import com.quangtrung.HireAI.service.BlogService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/blogs")
public class BlogsController {

    @Autowired
    BlogService blogService;

    @Autowired
    CategoryService categoryService;

    @ModelAttribute("categories")
    public List<CategoryDTO> categories() {
        return categoryService.findAll().stream().map(item -> {
            CategoryDTO dto = new CategoryDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).toList();
    }

    @RequestMapping("")
    public String showBlogs(Model model, @RequestParam(name = "name", required = false) String name,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size) {

        model.addAttribute("activeMenu", "blogs");

        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogPage;

        if (StringUtils.hasText(name)) {
            blogPage = blogService.findByTitleContaining(name, pageable);
        } else {
            blogPage = blogService.findPaginated(pageable);
        }

        model.addAttribute("page", blogPage);
        return "admin/blogs/Blogs";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("blog", new BlogDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "blogs");
        return "admin/blogs/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("blog") BlogDTO dto, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            if (dto.getBlog_id() == null) {
                model.addAttribute("isAdd", true);
            } else {
                model.addAttribute("isEdit", true);
            }
            model.addAttribute("activeMenu", "blogs");
            return new ModelAndView("admin/blogs/Modal", model);
        }
        Blog entity = new Blog();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getBlog_id() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        Category category = new Category();
        category.setCategory_id(dto.getCategory_id());
        entity.setCategory(category);

        blogService.save(entity);
        return new ModelAndView("redirect:/admin/blogs", model);
    }

    @GetMapping("/edit/{blog_id}")
    public ModelAndView edit(ModelMap model, @PathVariable("blog_id") Long id) {
        Optional<Blog> opt = blogService.findById(id);
        BlogDTO dto = new BlogDTO();

        if (opt.isPresent()) {
            Blog entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("blog", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "blogs");
            return new ModelAndView("admin/blogs/Modal", model);
        }
        model.addAttribute("message", "Blog is not existed!");
        return new ModelAndView("forward:/admin/blogs", model);
    }

    @GetMapping("/delete/{blog_id}")
    public ModelAndView delete(ModelMap model, @PathVariable("blog_id") Long id) {
        blogService.deleteById(id);
        model.addAttribute("message", "Blog is deleted!");
        return new ModelAndView("redirect:/admin/blogs", model);
    }
}
