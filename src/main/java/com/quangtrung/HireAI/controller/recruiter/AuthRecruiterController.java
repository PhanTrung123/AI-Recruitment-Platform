package com.quangtrung.HireAI.controller.recruiter;

import com.quangtrung.HireAI.dto.AccountDTO;
import com.quangtrung.HireAI.dto.CompanyDTO;
import com.quangtrung.HireAI.dto.LoginDTO;
import com.quangtrung.HireAI.dto.SessionUserDTO;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.enums.Role;
import com.quangtrung.HireAI.service.CompanyService;
import com.quangtrung.HireAI.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employer")
public class AuthRecruiterController {

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @Autowired
    HttpSession session;

    @GetMapping("/signup")
    public String showEmployerSignup(Model model) {
        model.addAttribute("company", new CompanyDTO());
        return "recruiter/signup/SignupEmp";
    }

    @GetMapping("/signup/add")
    public String showSignUpCompany(Model model) {
        model.addAttribute("company", new CompanyDTO());
        return "recruiter/signup/SignupEmp";
    }

    @PostMapping("/signup/add")
    public String signUpCompany(@Valid @ModelAttribute("company") CompanyDTO dto, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("company", dto);
            return "recruiter/signup/SignupEmp";
        }

        // Create account with USER role (will be upgraded to RECRUITER after admin approval)
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserName(dto.getUserName());
        accountDTO.setEmail(dto.getUserEmail());
        accountDTO.setPassword(dto.getPassword());
        accountDTO.setPhone(dto.getUserPhone());
        User user = userService.save(accountDTO, com.quangtrung.HireAI.enums.Role.USER);

        dto.setUserId(user.getId());
        companyService.saveFromDTO(dto);

        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký doanh nghiệp thành công! Đăng nhập để tiếp tục thiết lập không gian tuyển dụng của công ty.");
        return "redirect:/employer/login";
    }

    @GetMapping("/login")
    public String showEmployerLogin(Model model) {
        if (!model.containsAttribute("login")) {
            model.addAttribute("login", new LoginDTO());
        }
        return "recruiter/login/LoginEmp";
    }

    @PostMapping("/login")
    public ModelAndView login(@Valid @ModelAttribute("login") LoginDTO loginDTO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return new ModelAndView("recruiter/login/LoginEmp", model);
        }

        User user = userService.login(loginDTO);
        if (user == null) {
            model.addAttribute("message", "Mật khẩu hoặc tên đăng nhập không chính xác!");
            return new ModelAndView("recruiter/login/LoginEmp", model);
        }

        if (!Role.RECRUITER.equals(user.getRole())) {
            model.addAttribute("message", "Tài khoản này không phải là nhà tuyển dụng!");
            return new ModelAndView("recruiter/login/LoginEmp", model);
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

        return new ModelAndView("redirect:/employer/dashboard", model);
    }

}
