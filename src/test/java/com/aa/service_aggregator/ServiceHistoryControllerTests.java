package com.aa.service_aggregator;

import com.aa.service_aggregator.controller.ServiceHistoryController;
import com.aa.service_aggregator.dto.ServiceRecordDTO;
import com.aa.service_aggregator.model.ServiceRecord;
import com.aa.service_aggregator.service.ServiceHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServiceHistoryControllerTest {

    private ServiceHistoryService serviceHistoryService;
    private ServiceHistoryController controller;

    @BeforeEach
    void setUp() {
        serviceHistoryService = mock(ServiceHistoryService.class);
        controller = new ServiceHistoryController(serviceHistoryService);
    }

    @Test
    void testIngestServiceHistory_withEmptyList_returnsBadRequest() {
        List<ServiceRecordDTO> input = Collections.emptyList();
        ResponseEntity<String> response = controller.ingestServiceHistory(input);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("No Service Records provided", response.getBody());
    }

    @Test
    void testIngestServiceHistory_allRecordsSuccess_returnsOk() {
        ServiceRecordDTO dto = new ServiceRecordDTO("1HGCM82633A004352","2024-09-12",45000,"Oil Change", 89.99);
        List<ServiceRecordDTO> input = List.of(dto);

        when(serviceHistoryService.addServiceHistory(input)).thenReturn(Collections.emptyList());

        ResponseEntity<String> response = controller.ingestServiceHistory(input);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Created 1 records"));
    }

    @Test
    void testIngestServiceHistory_someRecordsFail_returnsPartialMessage() {
        ServiceRecordDTO dto = new ServiceRecordDTO("1HGCM82633A004352","2024-09-12",45000,"Oil Change", 89.99);
        List<ServiceRecordDTO> input = List.of(dto);

        when(serviceHistoryService.addServiceHistory(input)).thenReturn(List.of(dto));

        ResponseEntity<String> response = controller.ingestServiceHistory(input);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Issues with 1 record"));
    }

    @Test
    void testGetRecordsByVin_noRecordsFound_returnsNotFound() {
        String vin = "123456";
        when(serviceHistoryService.getRecordsByVin(vin)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ServiceRecord>> response = controller.getRecordsByVin(vin);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetRecordsByVin_recordsFound_returnsOk() {
        String vin = "123456";
        ServiceRecord record = new ServiceRecord(); // Set fields if needed
        List<ServiceRecord> records = List.of(record);

        when(serviceHistoryService.getRecordsByVin(vin)).thenReturn(records);

        ResponseEntity<List<ServiceRecord>> response = controller.getRecordsByVin(vin);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(records, response.getBody());
    }
}