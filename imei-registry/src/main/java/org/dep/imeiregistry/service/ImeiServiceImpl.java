package org.dep.imeiregistry.service;

import org.dep.imeiregistry.dto.ImeiRequest;
import org.dep.imeiregistry.dto.ImeiResponse;
import org.dep.imeiregistry.dto.RegistryBatchItem;
import org.dep.imeiregistry.dto.RowResult;
import org.dep.imeiregistry.exception.ImeiNotFoundException;
import org.dep.imeiregistry.model.Imei;
import org.dep.imeiregistry.repository.ImeiRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImeiServiceImpl implements ImeiService {

    private final ImeiRepository repo;

    public ImeiServiceImpl(ImeiRepository repo) {
        this.repo = repo;
    }

    @Override
    public ImeiResponse blockImei(ImeiRequest dto) {
        return updateImeiStatus(dto, "Blocked");
    }

    @Override
    public ImeiResponse unblockImei(ImeiRequest dto) {
        return updateImeiStatus(dto, "Unblocked");
    }

    @Override
    public String getStatus(String imei) {
        Imei record = repo.findByImeiNumber(imei)
                .orElseThrow(() -> new ImeiNotFoundException("IMEI not found: " + imei));
        return record.getStatus();
    }

    @Override
    public Imei getImei(String imei) {
        return repo.findByImeiNumber(imei)
                .orElseThrow(() -> new ImeiNotFoundException("IMEI not found: " + imei));
    }

    @Override
    public List<RowResult> processBatch(List<RegistryBatchItem> batch) {
        List<RowResult> results = new ArrayList<>();

        for (RegistryBatchItem item : batch) {
            try {
                ImeiRequest request = item.getRecord();

                if ("BLOCK".equalsIgnoreCase(request.getAction())) {
                    blockImei(request);
                } else if ("UNBLOCK".equalsIgnoreCase(request.getAction())) {
                    unblockImei(request);
                } else {
                    results.add(new RowResult(
                            item.getRowNumber(),
                            "FAILED",
                            "Invalid action: " + request.getAction()
                    ));
                    continue;
                }

                results.add(new RowResult(
                        item.getRowNumber(),
                        "SUCCESS",
                        null
                ));

            } catch (Exception e) {
                results.add(new RowResult(
                        item.getRowNumber(),
                        "FAILED",
                        e.getMessage()
                ));
            }
        }

        return results;
    }

    /**
     * Finds an existing device record by IMEI or creates a new one, then updates status and action.
     */
    private ImeiResponse updateImeiStatus(ImeiRequest dto, String status) {
        Imei record = repo.findByImeiNumber(dto.getImeiNumber())
                .orElseGet(() -> Imei.builder()
                        .imeiNumber(dto.getImeiNumber())
                        .userName(dto.getUserName())
                        .mobileModel(dto.getMobileModel())
                        .telecomCompany(dto.getTelecomCompany())
                        .build()
                );

        record.setStatus(status);
        record.setLastAction(status);

        Imei savedRecord = repo.save(record);

        return ImeiResponse.builder()
                .imeiNumber(savedRecord.getImeiNumber())
                .status(savedRecord.getStatus())
                .userName(savedRecord.getUserName())
                .mobileModel(savedRecord.getMobileModel())
                .build();
    }
}
