package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsDeleteMessage;
import com.eric6166.aws.sqs.SqsDeleteMessages;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsBatchDeleteRequest implements SqsDeleteMessages {
    String queueName;
    Integer maxNumberOfMessages;
    Collection<Message> messages;

    @Override
    public Collection<SqsDeleteMessage> getSqsDeleteMessages() {
        return messages.stream().map(o -> (SqsDeleteMessage) o).collect(Collectors.toList());
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message implements SqsDeleteMessage {
        String receiptHandle;
        String id;

    }
}
