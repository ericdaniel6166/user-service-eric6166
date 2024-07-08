package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsSendMessageBatchRequestEntries;
import com.eric6166.aws.sqs.SqsSendMessageBatchRequestEntry;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collection;

@Data
public class TestSqsBatchRequest implements SqsSendMessageBatchRequestEntries {

    @NotBlank
    @NotEmpty
    private Collection<Message> messages;
    @NotNull
    private Integer delaySeconds;
    @NotBlank
    private String queueName;
    @NotBlank
    private String messageGroupId;

    @Override
    public Collection<SqsSendMessageBatchRequestEntry> getSqsSendMessageBatchRequestEntries() {
        return messages.stream().map(o -> (SqsSendMessageBatchRequestEntry) o).toList();
    }

    @Data
    public static class Message implements SqsSendMessageBatchRequestEntry {
        private String messageBody;
        private String id;

    }
}
