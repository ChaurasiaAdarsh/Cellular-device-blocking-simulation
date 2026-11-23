package org.dep.fileingestion.service;

import org.dep.fileingestion.dto.FileRecordDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataValidator {
//this validates all things in a row
    public Optional<String> validate(FileRecordDTO dto, int rowNum) {

        if (dto == null) {
            return Optional.of("Row is empty");
        }

        if (isBlank(dto.getUserName())) {
            return Optional.of("Username is blank");
        }

        if (isBlank(dto.getMobileModel())) {
            return Optional.of("Mobile model is blank");
        }

        if (isBlank(dto.getImeiNumber())) {
            return Optional.of("IMEI number is blank");
        }

        if (isBlank(dto.getTelecomCompany())) {
            return Optional.of("Telecom company is blank");
        }

        if (isBlank(dto.getAction())) {
            return Optional.of("Action is blank");
        }

        if (!dto.getImeiNumber().matches("\\d{15}")) {
            return Optional.of(
                    "IMEI number must contain exactly 15 digits"
            );
        }

        if (!dto.getAction().equalsIgnoreCase("BLOCK")
                && !dto.getAction().equalsIgnoreCase("UNBLOCK")) {

            return Optional.of(
                    "Action must be BLOCK or UNBLOCK"
            );
        }

        return Optional.empty();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}