package com.eric6166.user.dto;

import com.eric6166.base.utils.AppDateUtils;
import com.eric6166.base.validation.ValidDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestPostFormRequest {
    @ValidDate(flag = ValidDate.Flag.DATE_TIME, pattern = AppDateUtils.DEFAULT_DATE_TIME_PATTERN)
    String dateTime;
    @ValidDate(flag = ValidDate.Flag.DATE, pattern = AppDateUtils.DEFAULT_DATE_PATTERN)
    String date;

}
