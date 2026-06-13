package com.example.its.web.information;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class InformationForm {

    @NotBlank
    @Size(max = 256)
    private String informationTitle;

    @NotBlank
    @Size(max = 2000)
    private String informationDetail;
}
