package user_management.annotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import user_management.entity.AuditLogEntity;
import user_management.repository.AuditLogRepository;


import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;

    @Around("@annotation(auditLogAction)")
    public Object log(ProceedingJoinPoint joinPoint, AuditLogAction auditLogAction) throws Throwable {

        log.info("AUDIT TRIGGERED");

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        AuditLogEntity log = new AuditLogEntity();
        log.setUsername(username);
        log.setAction(auditLogAction.action());
        log.setCreatedAt(LocalDateTime.now());
        log.setStatus("SUCCESS");

        try {
            Object result = joinPoint.proceed();
            auditLogRepository.save(log);
            return result;

        } catch (Exception ex) {
            log.setStatus("FAILED");
            auditLogRepository.save(log);
            throw ex;
        }
    }
}
