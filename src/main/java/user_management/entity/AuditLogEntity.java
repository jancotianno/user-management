package user_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "audit_log")
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String action;
    private String entityName;
    private Long entityId;
    private String endpoint;
    private String httpMethod;
    private String status;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "jsonb")
    private String details;

}
