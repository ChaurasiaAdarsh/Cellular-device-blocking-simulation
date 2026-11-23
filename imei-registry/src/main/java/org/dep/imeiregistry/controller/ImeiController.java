package org.dep.imeiregistry.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.dep.imeiregistry.dto.ImeiRequest;
import org.dep.imeiregistry.dto.ImeiResponse;
import org.dep.imeiregistry.dto.RegistryBatchItem;
import org.dep.imeiregistry.dto.RowResult;
import org.dep.imeiregistry.model.Imei;
import org.dep.imeiregistry.service.ImeiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/imei")
public class ImeiController {

    private final ImeiService service;

    public ImeiController(ImeiService service) {
        this.service = service;
    }

    @PostMapping("/block")
    public ResponseEntity<ImeiResponse> blockImei(@Valid @RequestBody ImeiRequest dto) {
        ImeiResponse response = service.blockImei(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unblock")
    public ResponseEntity<ImeiResponse> unblockImei(@Valid @RequestBody ImeiRequest dto) {
        ImeiResponse response = service.unblockImei(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{imei}/status")
    public ResponseEntity<String> getImeiStatus(@PathVariable String imei) {
        String status = service.getStatus(imei);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{imei}")
    public ResponseEntity<Imei> getImei(
            @Size(min = 15, max = 15, message = "IMEI must be exactly 15 digits")
            @PathVariable String imei) {
        Imei record = service.getImei(imei);
        return ResponseEntity.ok(record);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<RowResult>> processBatch(
            @RequestBody List<RegistryBatchItem> batch) {
        List<RowResult> results = service.processBatch(batch);
        return ResponseEntity.ok(results);
    }
}
