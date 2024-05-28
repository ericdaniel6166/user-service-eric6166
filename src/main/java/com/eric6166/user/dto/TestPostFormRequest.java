package com.eric6166.user.dto;

import com.eric6166.base.utils.DateTimeUtils;
import com.eric6166.base.validation.ValidDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestPostFormRequest {
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE_TIME, pattern = DateTimeUtils.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    String dateTime;
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE, pattern = DateTimeUtils.DEFAULT_LOCAL_DATE_PATTERN)
    String date;

}
