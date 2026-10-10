package com.quangtrung.HireAI.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryDTO {
    private Long category_id;

    @NotEmpty(message = "category name cannot be empty")
    @Length(min = 5, message = "category name must be at least 5 characters long")
    private String categoryName;

    @NotEmpty(message = "description cannot be empty")
    @Length(min = 20, message = "description must be at least 20 characters long")
    private String description;

    private Boolean isEdit = false;

}
