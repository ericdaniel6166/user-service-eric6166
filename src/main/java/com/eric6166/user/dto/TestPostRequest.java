package com.eric6166.user.dto;

import com.eric6166.base.utils.DateTimeUtils;
import com.eric6166.base.utils.BaseConst;
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

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestPostRequest {
    LocalDateTime dateTime;
    LocalDate date;
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE_TIME, pattern = DateTimeUtils.DEFAULT_DATE_TIME_PATTERN)
    String dateTimeStr;
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
    @ValidNumber(flag = ValidNumber.Flag.INTEGER)
    String longStr;


}
