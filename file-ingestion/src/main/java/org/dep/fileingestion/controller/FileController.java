package org.dep.fileingestion.controller;

import org.dep.fileingestion.client.RegistryClient;
import org.dep.fileingestion.dto.BatchResultDTO;
import org.dep.fileingestion.dto.FileRecordDTO;
import org.dep.fileingestion.dto.RegistryBatchItem;
import org.dep.fileingestion.dto.RowResult;
import org.dep.fileingestion.service.DataValidator;
import org.dep.fileingestion.service.FileParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileParser fileParser;
    private final DataValidator dataValidator;
    private final RegistryClient registryClient;
    private final ExecutorService registryExecutorService;

    @Value("${registry.batch-size:10}")
    private int batchSize;

    public FileController(FileParser fileParser,DataValidator dataValidator,RegistryClient registryClient,ExecutorService registryExecutorService) {
        this.fileParser = fileParser;
        this.dataValidator = dataValidator;
        this.registryClient = registryClient;
        this.registryExecutorService = registryExecutorService;
    }

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<BatchResultDTO>> uploadFile(
            @RequestParam("file") MultipartFile file) {

        //  Parse file
        List<FileRecordDTO> records =fileParser.parse(file);
        List<RegistryBatchItem> validRecords = new ArrayList<>();
        List<RowResult> results = new ArrayList<>();

        //  Validate every row
        for (int i = 0; i < records.size(); i++) {

            FileRecordDTO record = records.get(i);
            // skips the 1st row that is header
            int rowNumber = i + 2;
            Optional<String> validationError = dataValidator.validate(record, rowNumber);

            if (validationError.isPresent()) {
                results.add(RowResult.builder()
                                .rowNumber(rowNumber)
                                .status("FAILED")
                                .reason(validationError.get())
                                .build());

            } else {

                validRecords.add(new RegistryBatchItem(rowNumber, record));
            }
        }

        //  Split valid records into batches
        List<List<RegistryBatchItem>> batches = createBatches(validRecords, batchSize);

        //  Create CompletableFuture for each BATCH
        List<CompletableFuture<List<RowResult>>> futures = new ArrayList<>();

        for (List<RegistryBatchItem> batch : batches) {

            CompletableFuture<List<RowResult>> future =
                    CompletableFuture.supplyAsync(() -> registryClient.sendBatch(batch),registryExecutorService)
                            .exceptionally(ex -> {
                                List<RowResult> failedResults = new ArrayList<>();

                                for (RegistryBatchItem item : batch) {
                                    failedResults.add(
                                            new RowResult(item.getRowNumber(),"FAILED","Registry service error: "+ ex.getMessage() )
                                    );
                                }
                                return failedResults;
                            });

            futures.add(future);
        }

        //  Wait for all batches
        CompletableFuture<Void> allBatches =CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        //  Combine results
        return allBatches.thenApply(v -> {
            for (CompletableFuture<List<RowResult>> future: futures) {
                results.addAll(future.join());
            }

            int failureCount = (int) results.stream()
                    .filter(result ->"FAILED".equals(result.getStatus()))
                    .count();

            int successCount =
                    records.size() - failureCount;

            BatchResultDTO result =
                    BatchResultDTO.builder()
                            .totalRows(records.size())
                            .successCount(successCount)
                            .failureCount(failureCount)
                            .results(results)
                            .build();

            return ResponseEntity.ok(result);
        });
    }

    private List<List<RegistryBatchItem>> createBatches(List<RegistryBatchItem> records,int batchSize) {
        List<List<RegistryBatchItem>> batches = new ArrayList<>();
        for (int i = 0; i < records.size(); i += batchSize) {
            int end = Math.min(i + batchSize, records.size());
            batches.add(records.subList(i, end));
        }
        return batches;
    }
}