package user_management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    public void sendWelcomeEmail(String username) {
        log.info("EMAIL SENT TO " + username);
    }
}
