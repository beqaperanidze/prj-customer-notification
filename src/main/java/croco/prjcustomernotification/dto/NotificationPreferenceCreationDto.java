package croco.prjcustomernotification.dto;

import croco.prjcustomernotification.enums.AddressType;
import croco.prjcustomernotification.enums.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceCreationDto {
    @NotNull
    private NotificationType type;

    @NotNull
    private AddressType channelType;

    private boolean optedIn;
}