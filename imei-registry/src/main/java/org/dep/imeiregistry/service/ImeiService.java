package org.dep.imeiregistry.service;

import org.dep.imeiregistry.dto.ImeiRequest;
import org.dep.imeiregistry.dto.ImeiResponse;
import org.dep.imeiregistry.dto.RegistryBatchItem;
import org.dep.imeiregistry.dto.RowResult;
import org.dep.imeiregistry.model.Imei;

import java.util.List;

public interface ImeiService {

    ImeiResponse blockImei(ImeiRequest dto);

    ImeiResponse unblockImei(ImeiRequest dto);

    String getStatus(String imei);

    Imei getImei(String imei);

    List<RowResult> processBatch(List<RegistryBatchItem> batch);
}
