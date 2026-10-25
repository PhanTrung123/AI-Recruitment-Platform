package com.quangtrung.HireAI.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CompanyDTO {
    private Long company_id;

    @NotEmpty(message = "Company name cannot be empty")
    @Length(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String name;

    @NotEmpty(message = "Logo cannot be empty")
    @Length(max = 500, message = "Logo must not exceed 500 characters")
    private String logo;

    private String slogan;
    private Integer foundedYear;
    private String industry;
    private String mapUrl;
    private List<String> benefits;

    @NotEmpty(message = "Description cannot be empty")
    @Length(min = 20, max = 1000, message = "Description must be between 20 and 1000 characters")
    private String description;

    @NotEmpty(message = "Website cannot be empty")
    @Length(min = 5, max = 255, message = "Website must be between 5 and 255 characters")
    private String website;

    @NotEmpty(message = "Company size cannot be empty")
    @Length(min = 1, max = 50, message = "Company size must be between 1 and 50 characters")
    private String size;

    @NotEmpty(message = "Address cannot be empty")
    @Length(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @NotEmpty(message = "Country cannot be empty")
    @Length(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    private String country;

    private Long userId;

    @NotEmpty(message = "Username cannot be empty")
    private String userName;

    @NotEmpty(message = "Email cannot be empty")
    private String userEmail;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Phone cannot be empty")
    private String userPhone;

    private Boolean isEdit = false;
}
