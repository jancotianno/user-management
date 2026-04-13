package user_management.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditLogAspect {

    @Around("@annotation(auditLog)")
    public Object logAudit(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {

        String username = "SYSTEM";

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            username = auth.getName();
        }

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long time = System.currentTimeMillis() - start;

            log.info("AUDIT | USER={} | ACTION={} | METHOD={} | TIME={}ms",
                    username,
                    auditLog.action(),
                    joinPoint.getSignature().getName(),
                    time
            );

            return result;

        } catch (Exception ex) {

            log.error("AUDIT FAILED | USER={} | ACTION={} | METHOD={} | ERROR={}",
                    username,
                    auditLog.action(),
                    joinPoint.getSignature().getName(),
                    ex.getMessage()
            );

            throw ex;
        }
    }
}
