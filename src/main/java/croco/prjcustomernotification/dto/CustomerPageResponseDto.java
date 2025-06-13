package croco.prjcustomernotification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPageResponseDto {
    private List<CustomerDto> customers;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

}