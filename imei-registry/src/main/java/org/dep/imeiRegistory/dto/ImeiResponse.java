package org.dep.imeiRegistory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


//its a DTO for the response

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImeiResponse {

    private String imeiNumber;
    private String userName;
    private String status;
    private String message;
    private String lastUpdated;
    private String mobileModel;
}