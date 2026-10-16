package com.quangtrung.HireAI.controller;

import com.quangtrung.HireAI.dto.SessionUserDTO;
import com.quangtrung.HireAI.dto.UserDTO;
import com.quangtrung.HireAI.entity.CV;
import com.quangtrung.HireAI.entity.User;
import com.quangtrung.HireAI.service.CVService;
import com.quangtrung.HireAI.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
public class ProfileController {

    @Autowired
    UserService userService;

    @Autowired
    CVService cvService;

    @GetMapping({"", "/{section}"})
    public String dashboard(@PathVariable(required = false) String section, Model model, HttpSession session) {
        String activeItem = resolveActiveItem(section);
        model.addAttribute("activeItem", activeItem);
        model.addAttribute("pageTitle", resolvePageTitle(activeItem));

        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userService.findById(sessionUser.getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<CV> latestCV = cvService.getLatestCVByUser(user);
            model.addAttribute("latestCV", latestCV.orElse(null));
        }

        if ("profile".equals(activeItem)) {
            UserDTO dto = new UserDTO();
            if (userOpt.isPresent()) {
                BeanUtils.copyProperties(userOpt.get(), dto);
            } else {
                dto.setId(sessionUser.getId());
                dto.setUserName(sessionUser.getUserName());
                dto.setEmail(sessionUser.getEmail());
                dto.setAvatar(sessionUser.getAvatar());
                dto.setGender(sessionUser.getGender());
                dto.setDateOfBirth(sessionUser.getDateOfBirth());
                dto.setAddress(sessionUser.getAddress());
                dto.setPersonalLink(sessionUser.getPersonalLink());
            }
            model.addAttribute("user", dto);

            return "client/dashboard/Profile";
        }
        return "client/dashboard/Dashboard";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute("user") UserDTO userDTO,
                                @RequestParam(value = "deleteAvatar", required = false, defaultValue = "false") String deleteAvatar,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        userDTO.setIsEdit(true);
        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        try {
            userService.updateProfile(sessionUser.getId(), userDTO, userDTO.getAvatarFile(), "true".equals(deleteAvatar));

            User updatedUser = userService.findById(sessionUser.getId()).orElse(null);
            if (updatedUser != null) {
                SessionUserDTO newSessionUser = new SessionUserDTO();
                BeanUtils.copyProperties(updatedUser, newSessionUser);
                session.setAttribute("account", newSessionUser);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật thất bại: " + e.getMessage());
        }

        return "redirect:/dashboard/profile";
    }

    @PostMapping("/cv/upload")
    @ResponseBody
    public Map<String, Object> uploadCV(@RequestParam("file") MultipartFile file, HttpSession session) {
        Map<String, Object> response = new java.util.HashMap<>();
        SessionUserDTO sessionUser = (SessionUserDTO) session.getAttribute("account");

        if (sessionUser == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để tải lên CV");
            return response;
        }

        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "Vui lòng chọn file CV");
            return response;
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("application/pdf") &&
                !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            response.put("success", false);
            response.put("message", "Chỉ chấp nhận file PDF hoặc DOCX");
            return response;
        }

        try {
            Optional<User> userOpt = userService.findById(sessionUser.getId());
            if (userOpt.isPresent()) {
                System.out.println("--> Đang tiến hành lưu CV cho user ID: " + userOpt.get().getId());

                CV cv = cvService.uploadCV(file, userOpt.get());

                System.out.println("--> Lưu CV thành công, ID bản ghi: " + cv.getCvs_id());

                response.put("success", true);
                response.put("message", "Tải lên CV thành công");
                response.put("cv", cv);
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi chi tiết ra console IntelliJ/Eclipse
            response.put("success", false);
            response.put("message", "Lỗi khi tải lên CV: " + e.getMessage());
        }

        return response;
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

}

