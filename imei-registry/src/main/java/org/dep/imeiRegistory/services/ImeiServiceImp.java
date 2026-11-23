package org.dep.imeiRegistory.services;

import org.dep.imeiRegistory.Repository.ImeiRepository;
import org.dep.imeiRegistory.dto.ImeiRequest;
import org.dep.imeiRegistory.dto.ImeiResponse;
import org.dep.imeiRegistory.dto.RegistryBatchItem;
import org.dep.imeiRegistory.dto.RowResult;
import org.dep.imeiRegistory.exception.ImeiNotFoundException;
import org.dep.imeiRegistory.model.Imei;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


//now this is the implementation class that implements ImeiService Interface
@Service
public class ImeiServiceImp implements ImeiService {

    private final ImeiRepository repo;
    public ImeiServiceImp(ImeiRepository repo) {
        this.repo = repo;
    }

    @Override
    public ImeiResponse blockImei(ImeiRequest dto) {
        return updateImeiStatus(dto,"Blocked");

    }

    @Override
    public ImeiResponse unblockImei(ImeiRequest dto) {
        return updateImeiStatus(dto,"Unblocked");
    }

    @Override
    public String getStatus(String imei) {
        Imei record = repo.findByImeiNumber(imei)
                .orElseThrow(() ->
                        new ImeiNotFoundException("IMEI not found: " + imei)
                );

        return record.getStatus();
    }

    @Override
    public Imei getImei(String imei) {
        return repo.findByImeiNumber(imei)
                .orElseThrow(() ->
                        new ImeiNotFoundException("IMEI not found: " + imei)
                );
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

                    results.add(
                            new RowResult(
                                    item.getRowNumber(),
                                    "FAILED",
                                    "Invalid action: " + request.getAction()
                            )
                    );

                    continue;
                }

                results.add(
                        new RowResult(
                                item.getRowNumber(),
                                "SUCCESS",
                                null
                        )
                );

            } catch (Exception e) {

                results.add(
                        new RowResult(
                                item.getRowNumber(),
                                "FAILED",
                                e.getMessage()
                        )
                );
            }
        }

        return results;
    }


    //This method is to distinguish when to save and when to update
    private ImeiResponse updateImeiStatus(ImeiRequest dto, String status) {

        // Find existing record or create a new one
        Imei record = repo.findByImeiNumber(dto.getImeiNumber())
                .orElseGet(() -> Imei.builder()
                        .imeiNumber(dto.getImeiNumber())
                        .userName(dto.getUserName())
                        .mobileModel(dto.getMobileModel())
                        .telecomCompany(dto.getTelecomCompany())
                        .build()
                );

        // Update status and last action performed
        record.setStatus(status);
        record.setLastAction(status);

        // Save record
        Imei savedRecord = repo.save(record);

        // Build response
        return ImeiResponse.builder()
                .imeiNumber(savedRecord.getImeiNumber())
                .status(savedRecord.getStatus())
                .userName(savedRecord.getUserName())
                .mobileModel(savedRecord.getMobileModel())
                .build();
    }
}
