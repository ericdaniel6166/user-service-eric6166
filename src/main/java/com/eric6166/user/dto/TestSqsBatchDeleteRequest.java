package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsDeleteMessageBatchRequestEntry;
import com.eric6166.aws.sqs.SqsDeleteMessageBatchRequestEntries;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsBatchDeleteRequest implements SqsDeleteMessageBatchRequestEntries {
    String queueName;
    Integer maxNumberOfMessages;
    Collection<Message> messages;

    @Override
    public Collection<SqsDeleteMessageBatchRequestEntry> getSqsDeleteMessageBatchRequestEntries() {
        return messages.stream().map(o -> (SqsDeleteMessageBatchRequestEntry) o).collect(Collectors.toList());
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message implements SqsDeleteMessageBatchRequestEntry {
        String receiptHandle;
        String id;

    }
}
