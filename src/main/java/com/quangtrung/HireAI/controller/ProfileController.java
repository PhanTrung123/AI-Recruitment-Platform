package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.UserDTO;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    UserService userService;

    @GetMapping({"/dashboard", "/dashboard/{section}"})
    public String dashboard(@PathVariable(required = false) String section, Model model) {
        String activeItem = resolveActiveItem(section);
        model.addAttribute("activeItem", activeItem);
        model.addAttribute("pageTitle", resolvePageTitle(activeItem));

        if ("profile".equals(activeItem)) {
            return "client/dashboard/Profile";
        }

        return "client/dashboard/Dashboard";
    }

    private String resolveActiveItem(String section) {
        if (section == null || section.isBlank() || "overview".equals(section)) {
            return "overview";
        }

        switch (section) {
            case "attachment":
                return "attachment";
            case "profile":
                return "profile";
            case "my-jobs":
                return "my-jobs";
            case "job-invitations":
                return "job-invitations";
            case "email-subscriptions":
                return "email-subscriptions";
            case "notifications":
                return "notifications";
            case "settings":
                return "settings";
            default:
                return "overview";
        }
    }

    private String resolvePageTitle(String activeItem) {
        switch (activeItem) {
            case "attachment":
                return "Hồ sơ đính kèm";
            case "profile":
                return "Hồ sơ ITviec";
            case "my-jobs":
                return "Việc làm của tôi";
            case "job-invitations":
                return "Lời mời công việc";
            case "email-subscriptions":
                return "Đăng ký nhận email";
            case "notifications":
                return "Thông báo";
            case "settings":
                return "Cài đặt";
            case "overview":
            default:
                return "Tổng quan";
        }
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editProfile(ModelMap model, @PathVariable("id") Long id) {
        Optional<User> opt = userService.findById(id);
        UserDTO dto = new UserDTO();

        if (opt.isPresent()) {
            User entity = opt.get();
            BeanUtils.copyProperties(entity, dto);
            dto.setIsEdit(true);
            model.addAttribute("user", dto);
            return new ModelAndView("client/dashboard/Profile", model);
        }
        model.addAttribute("message", "User is not existed!");
        return new ModelAndView("redirect:/dashboard/profile", model);
    }
}

