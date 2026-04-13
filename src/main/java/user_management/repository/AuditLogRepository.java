package user_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import user_management.entity.AuditLogEntity;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
}
