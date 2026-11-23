package org.dep.imeiregistry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImeiRequest {

    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "Mobile model cannot be blank")
    private String mobileModel;

    @NotBlank(message = "IMEI number cannot be blank")
    @Size(min = 15, max = 15, message = "IMEI number must be exactly 15 digits")
    @Pattern(regexp = "\\d{15}", message = "IMEI number must contain digits only")
    private String imeiNumber;

    @NotBlank(message = "Telecom company cannot be blank")
    private String telecomCompany;

    @NotBlank(message = "Action cannot be blank")
    private String action;
}
