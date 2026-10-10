package com.quangtrung.HireAI.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SkillDTO {
    private Long skill_id;

    @NotEmpty(message = "skill name cannot be empty")
    @Length(min = 5, message = "skill name must be at least 5 characters long")
    private String skillName;

    @NotEmpty(message = "description cannot be empty")
    @Length(min = 20, message = "skill name must be at least 20 characters long")
    private String description;

    private Boolean isEdit = false;

}
