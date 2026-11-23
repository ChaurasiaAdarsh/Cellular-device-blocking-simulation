package org.dep.fileingestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileRecordDTO {

    private String userName;
    private String mobileModel;
    private String imeiNumber;
    private String telecomCompany;
    private String action;


    }
