package com.eric6166.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TestAWSUploadRequest {

    private String bucket;
    private MultipartFile file;
    private String key;


}
