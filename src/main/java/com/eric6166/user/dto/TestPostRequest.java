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
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TestPostRequest {
    private LocalDateTime dateTime;
    private LocalDate date;
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE_TIME, pattern = DateTimeUtils.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    private String dateTimeStr;
    @ValidDateTime(flag = ValidDateTime.Flag.ZONED_DATE_TIME, pattern = DateTimeUtils.DEFAULT_DATE_TIME_PATTERN)
    private String zonedDateTimeStr;

    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE, pattern = DateTimeUtils.DEFAULT_LOCAL_DATE_PATTERN)
    private String dateStr;
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_TIME, pattern = DateTimeUtils.DEFAULT_LOCAL_TIME_PATTERN)
    private String timeStr;

    @Digits(integer = BaseConst.MAXIMUM_BIG_DECIMAL_INTEGER, fraction = BaseConst.MAXIMUM_BIG_DECIMAL_FRACTION)
    @Positive
    private BigDecimal number;
    private Long longNumber;
    private Integer integerNumber;

    @Min(value = 1)
    @Max(value = 10)
    @ValidNumber(flag = ValidNumber.Flag.PARSEABLE)
    private String numberStr;
    @ValidNumber(flag = ValidNumber.Flag.DIGITS)
    private String digitStr;
    @ValidNumber(flag = ValidNumber.Flag.BIG_INTEGER)
    private String longStr;

    @ValidDateTime(formatter = AppDateTimeFormatter.BASIC_ISO_DATE)
    private String basicIsoDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_LOCAL_DATE)
    private String isoLocalDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_OFFSET_DATE)
    private String isoOffsetDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_DATE)
    private String isoDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_LOCAL_TIME)
    private String isoLocalTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_OFFSET_TIME)
    private String isoOffsetTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_TIME)
    private String isoTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_LOCAL_DATE_TIME)
    private String isoLocalDateTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_OFFSET_DATE_TIME)
    private String isoOffsetDateTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_ZONED_DATE_TIME)
    private String isoZonedDateTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_DATE_TIME)
    private String isoDateTime;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_ORDINAL_DATE)
    private String isoOrdinalDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_WEEK_DATE)
    private String isoWeekDate;
    @ValidDateTime(formatter = AppDateTimeFormatter.ISO_INSTANT)
    private String isoInstant;
    @ValidDateTime(formatter = AppDateTimeFormatter.RFC_1123_DATE_TIME)
    private String rfc1123DateTime;

    private List<String> zoneIds;
    private String zoneId;


}
