package com.example.its.web.inquiry;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class InquiryForm {

    @NotBlank
    @Size(max=256)
    private String name;

    @NotBlank
    @Size(max=256)
    private String email;

    @NotBlank
    @Size(max=256)
    private String subject;

    @NotBlank
    @Size(max=256)
    private String message;

}
