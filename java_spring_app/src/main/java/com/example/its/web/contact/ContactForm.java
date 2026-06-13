package com.example.its.web.contact;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ContactForm {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Email
    @Size(max = 256)
    private String email;

    @NotBlank
    @Size(max = 256)
    private String subject;

    @NotBlank
    @Size(max = 2000)
    private String message;
}
