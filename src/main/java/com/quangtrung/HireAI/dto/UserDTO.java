package com.quangtrung.HireAI.dto;

import com.quangtrung.HireAI.enums.Gender;
import com.quangtrung.HireAI.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data

public class UserDTO {

    private Long id;

    @NotEmpty(message = "Tên người dùng không được để trống")
    @Size(min = 3, max = 50, message = "Tên người dùng phải từ 3 đến 50 ký tự")
    private String userName;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String password;

    @NotEmpty(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phone;

    private String avatar;

    private MultipartFile avatarFile;

    @NotNull(message = "Vai trò không được để trống")
    private Role role;

    private LocalDate dateOfBirth;

    @NotNull(message = "Giới tính không được để trống")
    private Gender gender;

    @NotEmpty(message = "Địa chỉ không được để trống")
    @Size(min = 5, max = 255, message = "Địa chỉ phải từ 5 đến 255 ký tự")
    private String address;

    private String personalLink;

    private Boolean isEdit = false;
}
