package croco.prjcustomernotification.model;

import croco.prjcustomernotification.enums.NotificationStatus;
import croco.prjcustomernotification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private String externalReferenceId;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private String failureReason;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}