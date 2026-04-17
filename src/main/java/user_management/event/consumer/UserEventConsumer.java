package user_management.event.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import user_management.event.model.UserCreatedEvent;
import user_management.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {

    private final EmailService emailService;

    @KafkaListener(
            topics = "${kafka.topic.user-created}",
            groupId = "user-group"
    )
    public void handleUserCreated(UserCreatedEvent event) {

        log.info("Evento USER_CREATED ricevuto: {}", event);

        emailService.sendWelcomeEmail(event.getUsername());
    }
}
