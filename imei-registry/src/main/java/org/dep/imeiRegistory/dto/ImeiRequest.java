package org.dep.imeiRegistory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ImeiRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String mobileModel;

    @NotBlank
    @Size(min = 15, max = 15, message = "IMEI number should be of 15 digits")
    private String imeiNumber;

    @NotBlank
    private String telecomCompany;

    @NotBlank
    private String action;
}