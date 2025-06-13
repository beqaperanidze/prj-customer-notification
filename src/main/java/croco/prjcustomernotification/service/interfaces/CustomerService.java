package croco.prjcustomernotification.service.interfaces;

import croco.prjcustomernotification.dto.CustomerCreationDto;
import croco.prjcustomernotification.dto.CustomerDto;
import croco.prjcustomernotification.dto.CustomerPageResponseDto;
import croco.prjcustomernotification.enums.NotificationType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface CustomerService {
    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(Long id);

    CustomerDto createCustomer(@Valid CustomerCreationDto customerDto);

    CustomerDto updateCustomer(Long id, @Valid CustomerCreationDto customerDto);

    void deleteCustomer(Long id);

    CustomerPageResponseDto searchCustomers(
            String name,
            String email,
            String phone,
            Set<NotificationType> optedInTypes,
            Pageable pageable);
}
