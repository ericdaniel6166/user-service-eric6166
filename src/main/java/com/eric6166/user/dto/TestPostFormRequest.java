package com.eric6166.user.dto;

import com.eric6166.base.utils.AppDateUtils;
import com.eric6166.base.validation.ValidDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestPostFormRequest {
    @ValidDateTime(flag = ValidDateTime.Flag.DATE_TIME, pattern = AppDateUtils.DEFAULT_DATE_TIME_PATTERN)
    String dateTime;
    @ValidDateTime(flag = ValidDateTime.Flag.DATE, pattern = AppDateUtils.DEFAULT_DATE_PATTERN)
    String date;

}
