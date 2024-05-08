package com.eric6166.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsRequest {

    String queueName;
    String queueUrl;
    String message;
    Integer delaySeconds;
    String messageGroupId;


}
