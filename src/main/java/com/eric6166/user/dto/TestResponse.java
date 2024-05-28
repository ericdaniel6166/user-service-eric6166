package com.eric6166.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResponse {

    LocalDateTime dateTime;
    ZonedDateTime zonedDateTime;
    Instant instant;
    Duration duration;
    MonthDay monthDay;
    OffsetTime offsetTime;
    OffsetDateTime offsetDateTime;
    YearMonth yearMonth;
    Year year;
    ZoneId zoneId;
    String zonedDateTimeStr;
    String zonedDateTime1;
    BigDecimal number;
    Double doubleNumber;
    Map<String, Object> zonedDateTimes;


}
