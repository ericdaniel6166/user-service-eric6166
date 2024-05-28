package com.eric6166.user.dto;

import com.eric6166.base.enums.AppDateTimeFormatter;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.utils.DateTimeUtils;
import com.eric6166.base.validation.ValidDateTime;
import com.eric6166.base.validation.ValidNumber;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestPostRequest {
    LocalDateTime dateTime;
    LocalDate date;
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE_TIME, pattern = DateTimeUtils.DEFAULT_DATE_TIME_PATTERN)
    String dateTimeStr;
    @ValidDateTime(flag = ValidDateTime.Flag.ZONED_DATE_TIME, pattern = DateTimeUtils.DEFAULT_ZONED_DATE_TIME_PATTERN)
    String zonedDateTimeStr;

    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE, pattern = DateTimeUtils.DEFAULT_DATE_PATTERN)
    String dateStr;
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_TIME, pattern = DateTimeUtils.DEFAULT_TIME_PATTERN)
    String timeStr;

    @Digits(integer = BaseConst.MAXIMUM_BIG_DECIMAL_INTEGER, fraction = BaseConst.MAXIMUM_BIG_DECIMAL_FRACTION)
    @Positive
    BigDecimal number;
    Long longNumber;
    Integer integerNumber;

    @Min(value = 1)
    @Max(value = 10)
    @ValidNumber(flag = ValidNumber.Flag.PARSEABLE)
    String numberStr;
    @ValidNumber(flag = ValidNumber.Flag.DIGITS)
    String digitStr;
    @ValidNumber(flag = ValidNumber.Flag.BIG_INTEGER)
    String longStr;

    @ValidDateTime(formatter = AppDateTimeFormatter.BASIC_ISO_DATE)
    String basicIsoDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_LOCAL_DATE)
    String isoLocalDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_OFFSET_DATE)
    String isoOffsetDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_DATE)
    String isoDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_LOCAL_TIME)
    String isoLocalTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_OFFSET_TIME)
    String isoOffsetTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_TIME)
    String isoTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_LOCAL_DATE_TIME)
    String isoLocalDateTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_OFFSET_DATE_TIME)
    String isoOffsetDateTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_ZONED_DATE_TIME)
    String isoZonedDateTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_DATE_TIME)
    String isoDateTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_ORDINAL_DATE)
    String isoOrdinalDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_WEEK_DATE)
    String isoWeekDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_INSTANT)
    String isoInstant;
    @ValidDateTime(formatter = AppDateTimeFormatter.RFC_1123_DATE_TIME)
    String rfc1123DateTime;

    List<String> zoneIds;


}
