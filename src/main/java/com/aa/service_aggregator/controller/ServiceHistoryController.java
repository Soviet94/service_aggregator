package com.aa.service_aggregator.controller;

import com.aa.service_aggregator.dto.ServiceRecordDTO;
import com.aa.service_aggregator.model.ServiceRecord;
import com.aa.service_aggregator.service.ServiceHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceHistoryController.class);
    private final ServiceHistoryService serviceHistoryService;

    @Autowired
    public ServiceHistoryController(ServiceHistoryService serviceHistoryService) {
        this.serviceHistoryService = serviceHistoryService;
    }

    @PostMapping()
    public ResponseEntity<String> ingestServiceHistory(@RequestBody List<ServiceRecordDTO> serviceInput) {
        logger.info("Processing service history POST request");

        //check for empty list
        if (serviceInput.isEmpty()){
            return ResponseEntity.badRequest().body("No Service Records provided");
        }

        List<ServiceRecordDTO> failed = this.serviceHistoryService.addServiceHistory(serviceInput);
        logger.info("Records created successfully: {}", failed);

        if (failed.isEmpty()){
            return ResponseEntity.ok("Created "+serviceInput.size()+" records");
        } else {
            return ResponseEntity.ok("Issues with "+failed.size()+" record(s). " + failed);
        }
    }

    @GetMapping(value = "/{vin}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceRecord>> getRecordsByVin(@PathVariable String vin) {
        logger.info("Searching service history for {}",vin);
        List<ServiceRecord> records = serviceHistoryService.getRecordsByVin(vin);

        if (records.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(records);
    }

}
