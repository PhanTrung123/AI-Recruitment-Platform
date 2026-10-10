package com.quangtrung.HireAI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/CVs")
public class CVsController {

    @GetMapping("")
    public String showCVs(Model model) {
        model.addAttribute("activeMenu", "CVs");
        return "admin/CVs/CVs";
    }

    @GetMapping("/add")
    public String addCVs(Model model) {
        model.addAttribute("isAdd", true);
        model.addAttribute("activeMenu", "CVs");
        return "admin/CVs/Modal";
    }

}
