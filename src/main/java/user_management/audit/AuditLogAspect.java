package user_management.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import user_management.entity.AuditLogEntity;
import user_management.repository.AuditLogRepository;

import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;

    private final ObjectMapper objectMapper;

    @Around("@annotation(auditLogAction)")
    public Object log(ProceedingJoinPoint joinPoint, AuditLogAction auditLogAction) throws Throwable {

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        Object[] args = joinPoint.getArgs();
        Long entityId = null;

        for (Object arg : args) {
            if (arg instanceof Long) {
                entityId = (Long) arg;
                break;
            }
        }

        String endpoint = request.getRequestURI();
        String method = request.getMethod();

        String details = objectMapper.writeValueAsString(joinPoint.getArgs());

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        AuditLogEntity log = new AuditLogEntity();
        log.setUsername(username);
        log.setAction(auditLogAction.action());
        log.setCreatedAt(LocalDateTime.now());
        log.setStatus("SUCCESS");
        log.setEndpoint(endpoint);
        log.setHttpMethod(method);
        log.setEntityId(entityId);
        log.setDetails(details);

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
