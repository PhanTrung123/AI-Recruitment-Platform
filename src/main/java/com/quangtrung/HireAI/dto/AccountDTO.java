package com.quangtrung.HireAI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AccountDTO {
    private Long id;

    @NotBlank(message = "Tên người dùng không được để trống")
    @Length(min = 3, max = 50, message = "Tên người dùng phải từ 3 đến 50 ký tự")
    private String userName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    // Custom annotation
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Length(min = 6, max = 100, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    private String phone;
}
