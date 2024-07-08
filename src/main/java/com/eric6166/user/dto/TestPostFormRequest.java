package com.eric6166.user.dto;

import com.eric6166.base.utils.DateTimeUtils;
import com.eric6166.base.validation.ValidDateTime;
import lombok.Data;

@Data
public class TestPostFormRequest {
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE_TIME, pattern = DateTimeUtils.DEFAULT_LOCAL_DATE_TIME_PATTERN)
    private String dateTime;
    @ValidDateTime(flag = ValidDateTime.Flag.LOCAL_DATE, pattern = DateTimeUtils.DEFAULT_LOCAL_DATE_PATTERN)
    private String date;

}
