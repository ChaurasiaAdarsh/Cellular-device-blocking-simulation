package org.dep.fileingestion.client;


import org.dep.fileingestion.dto.FileRecordDTO;
import org.dep.fileingestion.dto.RegistryBatchItem;
import org.dep.fileingestion.dto.RowResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Component
public class RegistryClient {

    private final RestTemplate restTemplate;

    @Value("${registry.service.url}")
    private String registryServiceUrl;

    public RegistryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RowResult> sendBatch(
            List<RegistryBatchItem> records) {

        String url = registryServiceUrl + "/api/v1/imei/batch";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<RegistryBatchItem>> request =
                new HttpEntity<>(records, headers);

        ResponseEntity<List<RowResult>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<List<RowResult>>() {}
                );

        return response.getBody();
    }
}
