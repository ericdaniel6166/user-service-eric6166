package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsDeleteMessageBatchRequestEntries;
import com.eric6166.aws.sqs.SqsDeleteMessageBatchRequestEntry;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class TestSqsBatchDeleteDto implements SqsDeleteMessageBatchRequestEntries {
    @NotEmpty
    private Collection<Message> messages;

    @Override
    public Collection<SqsDeleteMessageBatchRequestEntry> getSqsDeleteMessageBatchRequestEntries() {
        return messages.stream().map(o -> (SqsDeleteMessageBatchRequestEntry) o).toList();
    }

    @Data
    public static class Message implements SqsDeleteMessageBatchRequestEntry {
        private String receiptHandle;
        private String id;

    }
}
