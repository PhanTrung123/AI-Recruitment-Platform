package com.quangtrung.HireAI.dto;

import com.quangtrung.HireAI.enums.City;
import com.quangtrung.HireAI.enums.Level;
import com.quangtrung.HireAI.enums.WorkType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;

@Data
public class JobDTO {
    private Long job_id;

    @NotEmpty(message = "Tên công việc không được để trống")
    @Length(max = 255, message = "Tên không được vượt quá 255 ký tự")
    private String jobName;

    @NotEmpty(message = "Địa chỉ không được để trống")
    @Length(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @NotEmpty(message = "Mô tả công việc không được để trống")
    @Length(min = 20, max = 5000, message = "Mô tả phải từ 20 đến 5000 ký tự")
    private String description;

    @NotNull(message = "Trạng thái chấp nhận không được để trống")
    private Boolean accepted;

    @NotNull(message = "Lương không được để trống")
    @Min(value = 0, message = "Lương không được nhỏ hơn 0")
    private Double salary;

    @NotEmpty(message = "Yêu cầu công việc không được để trống")
    @Length(min = 10, max = 3000, message = "Yêu cầu phải từ 10 đến 3000 ký tự")
    private String requirements;

    @NotEmpty(message = "Quyền lợi không được để trống")
    @Length(min = 10, max = 3000, message = "Quyền lợi phải từ 10 đến 3000 ký tự")
    private String benefits;

    @NotNull(message = "Ngày hết hạn không được để trống")
    @Future(message = "Ngày hết hạn phải là một ngày trong tương lai")
    private LocalDate expiredAt;

    @NotNull(message = "Hình thức làm việc không được để trống")
    private WorkType workType;

    @NotNull(message = "Cấp bậc không được để trống")
    private Level level;

    @NotNull(message = "Thành phố không được để trống")
    private City city;

    @NotNull(message = "ID chuyên môn không được để trống")
    private Long expertisies_id;

    @NotNull(message = "ID vị trí không được để trống")
    private Long title_id;

    @NotEmpty(message = "ID lĩnh vực không được để trống")
    private List<Long> domain_id;

    @NotEmpty(message = "ID kỹ năng không được để trống")
    private List<Long> skill_id;

    @NotNull(message = "ID công ty không được để trống")
    private Long company_id;

    private Long applicationCount = 0L;

    private Boolean isEdit = false;
}
