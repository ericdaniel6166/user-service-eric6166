package com.eric6166.user.dto;

import com.eric6166.aws.sqs.SqsMessage;
import com.eric6166.aws.sqs.SqsMessages;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestSqsBatchRequest implements SqsMessages {

    String queueName;
    Collection<Message> messages;
    Integer delaySeconds;
    String messageGroupId;

    @Override
    public Collection<SqsMessage> getSqsMessages() {
        return messages.stream().map(o -> (SqsMessage) o).collect(Collectors.toList());
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Message implements SqsMessage {
        String messageBody;

        @Override
        public Integer getDelaySeconds() {
            return null;
        }

        @Override
        public String getId() {
            return UUID.randomUUID().toString();
        }

        @Override
        public String getMessageGroupId() {
            return null;
        }
    }
}
