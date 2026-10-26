package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.AccountDTO;
import com.quangtrung.HireAI.dto.LoginDTO;
import com.quangtrung.HireAI.dto.SessionUserDTO;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.enums.Role;
import com.quangtrung.HireAI.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    HttpSession session;

    @GetMapping("/sign_up")
    public String showSignUp(Model model) {
        model.addAttribute("sign_up", new AccountDTO());
        return "client/signup/SignUp";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "client/login/Login";
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute("login") LoginDTO loginDTO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return new ModelAndView("client/login/Login", model);
        }

        User user = userService.login(loginDTO);
        if (user == null) {
            model.addAttribute("message", "Mật khẩu hoặc tên đăng nhập không chính xác!");
            return new ModelAndView("client/login/Login", model);
        }

        SessionUserDTO sessionUser = new SessionUserDTO();
        sessionUser.setId(user.getId());
        sessionUser.setUserName(user.getUserName());
        sessionUser.setEmail(user.getEmail());
        sessionUser.setAvatar(user.getAvatar());
        sessionUser.setRole(user.getRole());
        sessionUser.setPhone(user.getPhone());
        sessionUser.setDateOfBirth(user.getDateOfBirth());
        sessionUser.setGender(user.getGender());
        sessionUser.setAddress(user.getAddress());
        sessionUser.setPersonalLink(user.getPersonalLink());

        session.setAttribute("account", sessionUser);
        session.setAttribute("checkLogin", true);

        if (Role.USER.equals(user.getRole())
                || Role.RECRUITER.equals(user.getRole())) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/admin", model);
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return new ModelAndView("redirect:/");
    }

    @PostMapping("/sign_up/save")
    public ModelAndView addOrUpdate(@Valid @ModelAttribute("sign_up") AccountDTO accountDTO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return new ModelAndView("client/signup/SignUp", model);
        }
        if (userService.existsByEmail(accountDTO.getEmail())) {
            result.rejectValue("email", "error.email", "Email này đã được đăng ký!");
            return new ModelAndView("client/signup/SignUp", model);
        }
        userService.save(accountDTO);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "admin/auth/403";
    }
}
