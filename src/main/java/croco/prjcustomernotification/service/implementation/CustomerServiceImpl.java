package croco.prjcustomernotification.service.implementation;

import croco.prjcustomernotification.dto.CustomerCreationDto;
import croco.prjcustomernotification.dto.CustomerDto;
import croco.prjcustomernotification.exception.CustomerNotFoundException;
import croco.prjcustomernotification.model.Customer;
import croco.prjcustomernotification.repository.CustomerRepository;
import croco.prjcustomernotification.service.interfaces.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        var customer = customerRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("Customer not found with id: " + id));
        return mapToDto(customer);
    }

    @Override
    public CustomerDto createCustomer(CustomerCreationDto customerDto) {
        Customer customer = Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .externalId(customerDto.getExternalId())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToDto(savedCustomer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerCreationDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setExternalId(customerDto.getExternalId());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return mapToDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    private CustomerDto mapToDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getExternalId());
    }
}