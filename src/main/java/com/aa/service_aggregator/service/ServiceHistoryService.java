package com.aa.service_aggregator.service;

import com.aa.service_aggregator.dto.ServiceRecordDTO;
import com.aa.service_aggregator.model.ServiceRecord;
import com.aa.service_aggregator.repository.ServiceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceHistoryService.class);
    private final ServiceRecordRepository serviceRecordRepository;

    @Autowired
    public ServiceHistoryService(ServiceRecordRepository serviceRecordRepository) {
        this.serviceRecordRepository = serviceRecordRepository;
    }

    public List<ServiceRecordDTO> addServiceHistory(List<ServiceRecordDTO> records) {
        List<ServiceRecordDTO> failed = new ArrayList<>();
        List<ServiceRecord> valid = new ArrayList<>();

        //validate the records
        for (ServiceRecordDTO record : records) {
            if (!valid(record)){
                failed.add(record);
                logger.warn("Failed record {}",record);
            } else {
                ServiceRecord tableRecord = new ServiceRecord(
                        record.vin(),
                        LocalDate.parse(record.service_date()),
                        record.mileage(),
                        record.service_type(),
                        record.service_cost()
                );
                valid.add(tableRecord);
            }
        }

        // Save all valid records to the database
        serviceRecordRepository.saveAll(valid);

        return failed;
    }

    private boolean valid(ServiceRecordDTO record){

        if (record.vin() == null || record.vin().length() < 17){
            return false;
        }

        if (record.service_date() == null){
            return false;
        }

        try {
            LocalDate.parse(record.service_date());
        } catch (DateTimeParseException exception){
            return false;
        }

        return true;
    }

    public List<ServiceRecord> getRecordsByVin(String vin) {
        return serviceRecordRepository.findByVinOrderByServiceDateAsc(vin);
    }
}
