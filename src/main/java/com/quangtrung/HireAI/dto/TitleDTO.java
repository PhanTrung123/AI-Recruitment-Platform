package com.quangtrung.HireAI.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class TitleDTO {
    private Long title_id;

    @NotEmpty(message = "title name cannot be empty")
    @Length(min = 3, message = "title name must be at least 3 characters long")
    private String titleName;

    @NotEmpty(message = "description cannot be empty")
    @Length(min = 10, message = "description must be at least 10 characters long")
    private String description;

    private Boolean isEdit = false;
}
