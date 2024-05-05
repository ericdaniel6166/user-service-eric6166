package com.eric6166.user.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestAWSUploadRequest {

    String bucket;
    MultipartFile file;
    String key;


}
