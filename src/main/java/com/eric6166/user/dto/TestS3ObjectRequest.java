package com.eric6166.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestS3ObjectRequest {

    String bucket;
    String sourceBucket;
    String destinationBucket;
    String sourceKey;
    String destinationKey;
    String key;
    Long signatureDuration; //mins


}
