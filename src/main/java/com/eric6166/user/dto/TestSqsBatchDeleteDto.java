package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsDeleteMessageBatchRequestEntries;
import com.eric6166.aws.sqs.SqsDeleteMessageBatchRequestEntry;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TestSqsBatchDeleteDto implements SqsDeleteMessageBatchRequestEntries {
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
