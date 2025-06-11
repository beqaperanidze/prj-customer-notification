package croco.prjcustomernotification.service.interfaces;

import croco.prjcustomernotification.dto.CustomerCreationDto;
import croco.prjcustomernotification.dto.CustomerDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(Long id);

    CustomerDto createCustomer(@Valid CustomerCreationDto customerDto);

    CustomerDto updateCustomer(Long id, @Valid CustomerCreationDto customerDto);

    void deleteCustomer(Long id);
}
