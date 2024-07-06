package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsSendMessageBatchRequestEntries;
import com.eric6166.aws.sqs.SqsSendMessageBatchRequestEntry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsBatchRequest implements SqsSendMessageBatchRequestEntries {

    @NotBlank
    @NotEmpty
    Collection<Message> messages;
    @NotNull
    Integer delaySeconds;
    @NotBlank
    String queueName;
    @NotBlank
    String messageGroupId;

    @Override
    public Collection<SqsSendMessageBatchRequestEntry> getSqsSendMessageBatchRequestEntries() {
        return messages.stream().map(o -> (SqsSendMessageBatchRequestEntry) o).toList();
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message implements SqsSendMessageBatchRequestEntry {
        String messageBody;
        String id;

    }
}
