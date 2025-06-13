package croco.prjcustomernotification.dto;

import croco.prjcustomernotification.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressCreationDto {
    @NotNull(message = "Address type is required")
    private AddressType type;

    @NotBlank(message = "Address value is required")
    private String value;

    private boolean primary;
}