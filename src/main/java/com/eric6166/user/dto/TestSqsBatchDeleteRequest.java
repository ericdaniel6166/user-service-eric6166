package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsDeleteMessageBatchRequestEntries;
import com.eric6166.aws.sqs.SqsDeleteMessageBatchRequestEntry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsBatchDeleteRequest implements SqsDeleteMessageBatchRequestEntries {
    @NotBlank
    String queueName;
    @NotNull
    Integer maxNumberOfMessages;
    @NotEmpty
    Collection<Message> messages;

    @Override
    public Collection<SqsDeleteMessageBatchRequestEntry> getSqsDeleteMessageBatchRequestEntries() {
        return messages.stream().map(o -> (SqsDeleteMessageBatchRequestEntry) o).toList();
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message implements SqsDeleteMessageBatchRequestEntry {
        String receiptHandle;
        String id;

    }
}
