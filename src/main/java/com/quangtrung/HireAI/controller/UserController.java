package com.quangtrung.HireAI.controller;


import com.quangtrung.HireAI.dto.UserDTO;
import com.quangtrung.HireAI.entity.User;
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

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("")
    public String showUsers(Model model, @RequestParam(name = "name", required = false) String name,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "5") int size) {
        model.addAttribute("activeMenu", "users");

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;
        if (StringUtils.hasText(name)) {
            userPage = userService.findByUserNameContaining(name, pageable);
        } else {
            userPage = userService.findPaginated(pageable);
        }
        model.addAttribute("page", userPage);
        return "admin/users/Users";
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "users");
        return "admin/users/Modal";
    }

    @PostMapping("/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("user") UserDTO dto, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            if (dto.getId() == null) {
                model.addAttribute("isAdd", true);
            } else {
                model.addAttribute("isEdit", true);
            }
            model.addAttribute("activeMenu", "users");
            return new ModelAndView("admin/users/Modal", model);
        }

        if (dto.getId() == null) {
            if (userService.existsByEmail(dto.getEmail())) {
                result.rejectValue("email", "error.email", "Email này đã được đăng ký! Vui lòng nhập email khác.");
                model.addAttribute("isAdd", true);
                model.addAttribute("activeMenu", "users");
                return new ModelAndView("admin/users/Modal", model);
            }
        } else {
            Optional<User> existingUser = userService.findById(dto.getId());
            if (existingUser.isPresent() && !existingUser.get().getEmail().equals(dto.getEmail())) {
                if (userService.existsByEmail(dto.getEmail())) {
                    result.rejectValue("email", "error.email", "Email này đã được đăng ký! Vui lòng nhập email khác.");
                    model.addAttribute("isEdit", true);
                    model.addAttribute("activeMenu", "users");
                    return new ModelAndView("admin/users/Modal", model);
                }
            }
        }

        userService.saveFullInfo(dto);
        return new ModelAndView("redirect:/admin/users", model);
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(ModelMap model, @PathVariable("id") Long id) {
        Optional<User> opt = userService.findById(id);
        UserDTO dto = new UserDTO();

        if (opt.isPresent()) {
            User entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("user", dto);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeMenu", "users");
            return new ModelAndView("admin/users/Modal", model);
        }
        model.addAttribute("message", "User is not existed!");
        return new ModelAndView("forward:/admin/users", model);
    }
    
    @GetMapping("/delete/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable("id") Long id) {
        userService.deleteById(id);
        model.addAttribute("message", "User is deleted!");
        return new ModelAndView("redirect:/admin/users", model);
    }


}
