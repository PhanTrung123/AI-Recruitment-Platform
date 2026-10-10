package com.quangtrung.HireAI.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ExpertiseDTO {
    private Long expertisies_id;

    @NotEmpty(message = "Expertise name cannot be empty")
    @Length(min = 5, max = 100, message = "Expertise name must be between 5 and 100 characters")
    private String ExpertiseName;

    @NotEmpty(message = "Description cannot be empty")
    @Length(min = 20, max = 500, message = "Description must be between 20 and 500 characters")
    private String description;

    private Boolean isEdit = false;

}
