package org.dep.fileingestion.service;

import org.dep.fileingestion.dto.FileRecordDTO;
import org.dep.fileingestion.exception.InvalidFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileParser {

    public List<FileRecordDTO> parse(MultipartFile file) {

        //  reject null or empty files
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File is empty or missing");
        }

        List<FileRecordDTO> records = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(file.getInputStream()))) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                // Split the line by comma
                // -1 limit preserves trailing empty fields
                String[] values = line.split(",", -1);

                if (values.length != 5) {
                    throw new InvalidFileException(
                            "Invalid CSV format. Each row must contain 5 fields."
                    );
                }

                // Map columns to DTO (trim whitespace from each value)
                FileRecordDTO dto = new FileRecordDTO();

                dto.setUserName(values[0].trim());
                dto.setMobileModel(values[1].trim());
                dto.setImeiNumber(values[2].trim());
                dto.setTelecomCompany(values[3].trim());
                dto.setAction(values[4].trim());

                records.add(dto);
            }

        } catch (InvalidFileException e) {
            throw e;

        } catch (Exception e) {
            throw new InvalidFileException(
                    "Unable to read the uploaded file"
            );
        }

        return records;
    }
}