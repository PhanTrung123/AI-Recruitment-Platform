package com.quangtrung.HireAI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class BlogDTO {
    private Long blog_id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Length(min = 5, max = 255, message = "Tiêu đề phải từ 5 đến 255 ký tự")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @Length(max = 500, message = "Đường dẫn thumbnail không được vượt quá 500 ký tự")
    private String thumbnail;

    @NotNull(message = "ID danh mục không được để trống")
    private Long category_id;

    private LocalDateTime createdAt;

    private Boolean isEdit = false;
}
