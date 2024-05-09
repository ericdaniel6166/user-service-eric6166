package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsSendMessageBatchRequestEntry;
import com.eric6166.aws.sqs.SqsSendMessageBatchRequestEntries;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsBatchRequest implements SqsSendMessageBatchRequestEntries {

    String queueName;
    Collection<Message> messages;
    Integer delaySeconds;
    String messageGroupId;

    @Override
    public Collection<SqsSendMessageBatchRequestEntry> getSqsSendMessageBatchRequestEntries() {
        return messages.stream().map(o -> (SqsSendMessageBatchRequestEntry) o).collect(Collectors.toList());
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message implements SqsSendMessageBatchRequestEntry {
        String messageBody;
        String id;

    }
}
