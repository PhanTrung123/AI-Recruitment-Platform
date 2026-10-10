package com.quangtrung.HireAI.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DomainDTO {
    private Long domain_id;

    @NotEmpty(message = "domain name cannot be empty")
    @Length(min = 5, message = "domain name must be at least 5 characters long")
    private String domainName;

    @NotEmpty(message = "description cannot be empty")
    @Length(min = 20, message = "domain name must be at least 20 characters long")
    private String description;

    private Boolean isEdit = false;

}
