package com.eric6166.user.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class KafkaProducerProps {

    @Value("${spring.kafka.producers.test-topic.topic-name}")
    private String testTopicName;

    @Value("${spring.kafka.template.producer.topic-name}")
    private String templateTopicName;


}
