package com.aa.service_aggregator.repository;

import com.aa.service_aggregator.model.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long> {
    List<ServiceRecord> findByVinOrderByServiceDateAsc(String vin);
}