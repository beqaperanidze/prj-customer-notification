package croco.prjcustomernotification.dto;

import croco.prjcustomernotification.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private AddressType type;
    private String value;
    private Long customerId;
    private boolean verified;
    private boolean primary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}