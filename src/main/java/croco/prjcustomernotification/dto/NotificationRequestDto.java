package croco.prjcustomernotification.dto;

import croco.prjcustomernotification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private Long customerId;
    private Long addressId;
    private NotificationType type;
    private String subject;
    private String content;
}