package user_management.event.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import user_management.event.model.UserCreatedEvent;

@Component
public class UserEventConsumer {

    @KafkaListener(
            topics = "${kafka.topic.userCreated}",
            groupId = "user-group"
    )
    public void consume(UserCreatedEvent event) {
        System.out.println("EVENT RECEIVED: " + event.getUserId());
    }
}
