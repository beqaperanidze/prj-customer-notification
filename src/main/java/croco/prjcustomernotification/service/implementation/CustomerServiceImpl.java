package croco.prjcustomernotification.service.implementation;

import croco.prjcustomernotification.dto.CustomerCreationDto;
import croco.prjcustomernotification.dto.CustomerDto;
import croco.prjcustomernotification.dto.CustomerPageResponseDto;
import croco.prjcustomernotification.enums.AddressType;
import croco.prjcustomernotification.enums.NotificationType;
import croco.prjcustomernotification.exception.ResourceNotFoundException;
import croco.prjcustomernotification.model.Address;
import croco.prjcustomernotification.model.Customer;
import croco.prjcustomernotification.model.NotificationPreference;
import croco.prjcustomernotification.repository.CustomerRepository;
import croco.prjcustomernotification.service.interfaces.CustomerService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
        var customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToDto(customer);
    }

    @Override
    public CustomerDto createCustomer(CustomerCreationDto customerDto) {
        Customer customer = Customer.builder().firstName(customerDto.getFirstName()).lastName(customerDto.getLastName()).externalId(customerDto.getExternalId()).build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToDto(savedCustomer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerCreationDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setExternalId(customerDto.getExternalId());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return mapToDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    @Override
    public CustomerPageResponseDto searchCustomers(String name, String email, String phone, Set<NotificationType> optedInTypes, Pageable pageable) {
        Specification<Customer> spec = Specification.allOf();

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, _, cb) -> cb.or(cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%"), cb.like(cb.lower(root.get("lastName")), "%" + name.toLowerCase() + "%")));
        }

        if (email != null && !email.isEmpty()) {
            spec = spec.and((root, _, cb) -> {
                Join<Customer, Address> addressJoin = root.join("addresses", JoinType.LEFT);
                return cb.and(cb.equal(addressJoin.get("type"), AddressType.EMAIL), cb.like(cb.lower(addressJoin.get("value")), "%" + email.toLowerCase() + "%"));
            });
        }

        if (phone != null && !phone.isEmpty()) {
            spec = spec.and((root, _, cb) -> {
                Join<Customer, Address> addressJoin = root.join("addresses", JoinType.LEFT);
                return cb.and(cb.equal(addressJoin.get("type"), AddressType.SMS), cb.like(addressJoin.get("value"), "%" + phone + "%"));
            });
        }

        if (optedInTypes != null && !optedInTypes.isEmpty()) {
            spec = spec.and((root, _, cb) -> {
                Join<Customer, NotificationPreference> preferenceJoin = root.join("notificationPreferences", JoinType.LEFT);
                return cb.and(preferenceJoin.get("type").in(optedInTypes), cb.isTrue(preferenceJoin.get("optedIn")));
            });
        }

        if ((email != null && !email.isEmpty()) || (phone != null && !phone.isEmpty()) || (optedInTypes != null && !optedInTypes.isEmpty())) {
            spec = spec.and((_, query, cb) -> {
                assert query != null;
                query.distinct(true);
                return cb.conjunction();
            });
        }

        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        List<CustomerDto> customerDtos = customerPage.getContent().stream().map(this::mapToDto).toList();

        return new CustomerPageResponseDto(customerDtos, customerPage.getNumber(), customerPage.getSize(), customerPage.getTotalElements(), customerPage.getTotalPages());
    }

    private CustomerDto mapToDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getExternalId());
    }
}