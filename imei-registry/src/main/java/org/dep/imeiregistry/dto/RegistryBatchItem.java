package org.dep.imeiregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistryBatchItem {

    private int rowNumber;
    private ImeiRequest record;
}
