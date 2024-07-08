package com.eric6166.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {

    private LocalDateTime dateTime;
    private ZonedDateTime zonedDateTime;
    private Instant instant;
    private Duration duration;
    private MonthDay monthDay;
    private OffsetTime offsetTime;
    private OffsetDateTime offsetDateTime;
    private YearMonth yearMonth;
    private Year year;
    private ZoneId zoneId;
    private String zonedDateTimeStr;
    private String zonedDateTime1;
    private BigDecimal number;
    private Double doubleNumber;
    private Map<String, Object> zonedDateTimes;


}
