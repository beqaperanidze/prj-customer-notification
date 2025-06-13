package croco.prjcustomernotification.dto;

import croco.prjcustomernotification.enums.NotificationStatus;
import croco.prjcustomernotification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private Long addressId;
    private String addressValue;
    private NotificationType type;
    private NotificationStatus status;
    private String externalReferenceId;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

