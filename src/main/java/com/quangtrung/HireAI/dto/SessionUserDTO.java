package com.quangtrung.HireAI.dto;

import com.quangtrung.HireAI.enums.Gender;
import com.quangtrung.HireAI.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String userName;
    private String email;
    private String avatar;
    private Role role;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String personalLink;
}
