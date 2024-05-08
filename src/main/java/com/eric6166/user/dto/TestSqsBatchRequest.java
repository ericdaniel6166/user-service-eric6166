package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsSendMessage;
import com.eric6166.aws.sqs.SqsSendMessages;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsBatchRequest implements SqsSendMessages {

    String queueName;
    Collection<Message> messages;
    Integer delaySeconds;
    String messageGroupId;

    @Override
    public Collection<SqsSendMessage> getSqsMessages() {
        return messages.stream().map(o -> (SqsSendMessage) o).collect(Collectors.toList());
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message implements SqsSendMessage {
        String messageBody;
        String id;

    }
}
