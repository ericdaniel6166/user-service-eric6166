package com.eric6166.user.dto;

import lombok.Data;

@Data
public class TestS3ObjectRequest {

    private String bucket;
    private String sourceBucket;
    private String destinationBucket;
    private String sourceKey;
    private String destinationKey;
    private String key;
    private Long signatureDuration; //mins


}
