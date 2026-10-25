package com.quangtrung.HireAI.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompanyWithSkillsDTO {
    private Long company_id;
    private String name;
    private String logo;
    private String address;
    private Integer jobCount;
    private List<String> topSkills;
    private Boolean isTopHiring;
}
