package com.aa.service_aggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ServiceRecordDTO(
        @JsonProperty("vin") String vin,
        @JsonProperty("service_date") String service_date,
        @JsonProperty("mileage") int mileage,
        @JsonProperty("service_type") String service_type,
        @JsonProperty("service_cost") double service_cost
) {}