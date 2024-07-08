package com.eric6166.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestSqsBatchDeleteRequest {
    @NotBlank
    private String queueName;
    @NotNull
    private Integer maxNumberOfMessages;
}
