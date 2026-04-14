package user_management.event.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import user_management.event.model.UserCreatedEvent;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.userCreated}")
    private String topic;

    public void sendUserCreatedEvent(UserCreatedEvent event) {
        kafkaTemplate.send(topic, event.getUserId().toString(), event);
    }
}
