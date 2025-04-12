package com.aa.service_aggregator;

import com.aa.service_aggregator.dto.ServiceRecordDTO;
import com.aa.service_aggregator.model.ServiceRecord;
import com.aa.service_aggregator.repository.ServiceRecordRepository;
import com.aa.service_aggregator.service.ServiceHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ServiceHistoryServiceTests {

    private ServiceRecordRepository serviceRecordRepository;
    private ServiceHistoryService serviceHistoryService;

    @BeforeEach
    void setUp() {
        serviceRecordRepository = mock(ServiceRecordRepository.class);
        serviceHistoryService = new ServiceHistoryService(serviceRecordRepository);
    }

    @Test
    void testAddServiceHistory_withValidAndInvalidRecords() {
        ServiceRecordDTO validRecord = new ServiceRecordDTO(
                "1HGCM82633A123456", "2023-05-12", 50000, "Oil Change", 49.99
        );

        ServiceRecordDTO invalidRecord = new ServiceRecordDTO(
                "123", "invalid-date", 30000, "Tire Rotation", 29.99
        );

        List<ServiceRecordDTO> failed = serviceHistoryService.addServiceHistory(
                Arrays.asList(validRecord, invalidRecord)
        );

        // Verify that repository.saveAll was called with only the valid record
        ArgumentCaptor<List<ServiceRecord>> captor = ArgumentCaptor.forClass(List.class);
        verify(serviceRecordRepository, times(1)).saveAll(captor.capture());
        List<ServiceRecord> savedRecords = captor.getValue();

        assertEquals(1, savedRecords.size());
        assertEquals("1HGCM82633A123456", savedRecords.get(0).getVin());

        // Check that the invalid record was returned in the failed list
        assertEquals(1, failed.size());
        assertEquals("123", failed.get(0).vin());
    }

    @Test
    void testAddServiceHistory_allValid() {
        ServiceRecordDTO record1 = new ServiceRecordDTO(
                "1HGCM82633A123456", "2024-01-15", 52000, "Brake Inspection", 89.99
        );

        List<ServiceRecordDTO> failed = serviceHistoryService.addServiceHistory(List.of(record1));

        verify(serviceRecordRepository, times(1)).saveAll(anyList());
        assertTrue(failed.isEmpty());
    }

    @Test
    void testAddServiceHistory_allInvalid() {
        ServiceRecordDTO record1 = new ServiceRecordDTO(
                null, "bad-date", 20000, "Alignment", 59.99
        );

        List<ServiceRecordDTO> failed = serviceHistoryService.addServiceHistory(List.of(record1));

        verify(serviceRecordRepository, never()).saveAll(anyList());
        assertEquals(1, failed.size());
    }

    @Test
    void testGetRecordsByVin() {
        String vin = "1HGCM82633A123456";
        List<ServiceRecord> mockResults = List.of(
                new ServiceRecord(vin, LocalDate.parse("2022-04-10"), 40000, "Oil Change", 39.99)
        );

        when(serviceRecordRepository.findByVinOrderByServiceDateAsc(vin)).thenReturn(mockResults);

        List<ServiceRecord> results = serviceHistoryService.getRecordsByVin(vin);

        assertEquals(1, results.size());
        assertEquals("1HGCM82633A123456", results.get(0).getVin());
    }
}