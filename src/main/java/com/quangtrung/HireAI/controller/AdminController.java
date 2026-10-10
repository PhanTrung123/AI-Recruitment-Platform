package com.quangtrung.HireAI.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
    @RequestMapping("/admin")
    public String showHome(Model model) {
        model.addAttribute("activeMenu", "dashboards");
        return "admin/dashboards/Dashboards";
    }


}
