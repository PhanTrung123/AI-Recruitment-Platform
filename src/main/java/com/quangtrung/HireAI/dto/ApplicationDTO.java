package com.quangtrung.HireAI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {
    private Long myjob_id;
    private String candidateName;
    private String candidateEmail;
    private String candidateAvatar;
    private Long job_id;
    private String jobName;
    private String cvFileUrl;
    private LocalDateTime appliedAt;
    private String status;
}
