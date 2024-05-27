package com.eric6166.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {

    LocalDateTime dateTime;
    String zonedDateTime;
    String zonedDateTime1;
    BigDecimal number;
    Double doubleNumber;
    Map<String, Object> zonedDateTimes;


}
