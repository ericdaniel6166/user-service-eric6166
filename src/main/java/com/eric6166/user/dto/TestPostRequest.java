package com.eric6166.user.dto;

import com.eric6166.base.utils.AppDateUtils;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.validation.ValidDate;
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
    @ValidDate(flag = ValidDate.Flag.DATE_TIME, pattern = AppDateUtils.DEFAULT_DATE_TIME_PATTERN)
    String dateTimeStr;
    @ValidDate(flag = ValidDate.Flag.DATE, pattern = AppDateUtils.DEFAULT_DATE_PATTERN)
    String dateStr;
    @Digits(integer = BaseConst.MAXIMUM_BIG_DECIMAL_INTEGER, fraction = BaseConst.MAXIMUM_BIG_DECIMAL_FRACTION)
    @Positive
    BigDecimal number;
    Long longNumber;
    Integer integerNumber;

    @Min(value = 1)
    @Max(value = 10)
    @ValidNumber(flag = ValidNumber.Flag.IS_PARSEABLE)
    String numberStr;
    @ValidNumber(flag = ValidNumber.Flag.IS_DIGITS)
    String digitStr;
    @ValidNumber(flag = ValidNumber.Flag.IS_INTEGER)
    String longStr;


}
