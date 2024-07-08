package com.eric6166.user.dto;

import lombok.Data;

@Data
public class TestSqsRequest {

    private String queueName;
    private String queueUrl;
    private String message;
    private Integer delaySeconds;
    private String messageGroupId;


}
