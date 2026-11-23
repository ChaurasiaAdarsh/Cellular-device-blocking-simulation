package org.dep.imeiRegistory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowResult {
    private int rowNumber;
    private String status;
    private String reason;
}
