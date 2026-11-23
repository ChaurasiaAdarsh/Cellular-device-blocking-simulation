package org.dep.imeiRegistory.services;

import org.dep.imeiRegistory.dto.ImeiRequest;
import org.dep.imeiRegistory.dto.ImeiResponse;
import org.dep.imeiRegistory.dto.RegistryBatchItem;
import org.dep.imeiRegistory.dto.RowResult;
import org.dep.imeiRegistory.model.Imei;

import java.util.List;

//this the interface that now comes between service and controller so that it follows solid principle now
public interface ImeiService {
    ImeiResponse blockImei(ImeiRequest dto);

    ImeiResponse unblockImei(ImeiRequest dto);

    String getStatus(String imei);

    Imei getImei(String imei);

    List<RowResult> processBatch(List<RegistryBatchItem> batch);
}
