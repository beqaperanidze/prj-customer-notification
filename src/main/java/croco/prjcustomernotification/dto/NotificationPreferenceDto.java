package croco.prjcustomernotification.dto;

import croco.prjcustomernotification.enums.AddressType;
import croco.prjcustomernotification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceDto {
    private Long id;
    private NotificationType type;
    private AddressType channelType;
    private boolean optedIn;
    private Long customerId;
}