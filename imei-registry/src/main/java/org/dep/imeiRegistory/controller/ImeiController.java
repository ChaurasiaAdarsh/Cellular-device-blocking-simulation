package org.dep.imeiRegistory.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.dep.imeiRegistory.dto.ImeiRequest;
import org.dep.imeiRegistory.dto.ImeiResponse;
import org.dep.imeiRegistory.dto.RegistryBatchItem;
import org.dep.imeiRegistory.dto.RowResult;
import org.dep.imeiRegistory.model.Imei;
import org.dep.imeiRegistory.services.ImeiService;
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

    //Sets the status as block
    @PostMapping("/block")
    public ResponseEntity<ImeiResponse> blockImei(@RequestBody ImeiRequest dto) {
        ImeiResponse response = service.blockImei(dto);
        return ResponseEntity.ok(response);
    }

    //Sets the status as unblock
    @PostMapping("/unblock")
    public ResponseEntity<ImeiResponse> unblockImei(@RequestBody ImeiRequest dto) {
        ImeiResponse response =service.unblockImei(dto);

        return ResponseEntity.ok(response);
    }

    //Returns just the status of a particular IMEI number
    @GetMapping("/{imei}/status")
    public ResponseEntity<String> getImeiStatus(@Valid @PathVariable String imei) {
        String status=service.getStatus(imei);
        return ResponseEntity.ok(status);
    }

    //Returns the full Record with status for a particular IMEI number
    @GetMapping("/{imei}")
    public ResponseEntity<Imei> getImei(@Size(max = 15 , min = 15, message = "Imei should be equal to 15")  @PathVariable String imei) {

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