package com.eric6166.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsBatchDeleteRequest {
    @NotBlank
    String queueName;
    @NotNull
    Integer maxNumberOfMessages;
}
