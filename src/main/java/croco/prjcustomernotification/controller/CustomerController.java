package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.CustomerCreationDto;
import croco.prjcustomernotification.dto.CustomerDto;
import croco.prjcustomernotification.dto.CustomerPageResponseDto;
import croco.prjcustomernotification.enums.NotificationType;
import croco.prjcustomernotification.service.interfaces.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Retrieves all customers.
     *
     * @return List of all customers.
     */
    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    /**
     * Retrieves a customer by ID.
     *
     * @param id Customer ID
     * @return Customer details
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    /**
     * Creates a new customer.
     *
     * @param customerDto Customer creation data
     * @return Created customer details
     */
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerCreationDto customerDto) {
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing customer.
     *
     * @param id          Customer ID
     * @param customerDto Updated customer data
     * @return Updated customer details
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerCreationDto customerDto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDto));
    }

    /**
     * Deletes a customer by ID.
     *
     * @param id Customer ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<CustomerPageResponseDto> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Set<NotificationType> optedInTypes,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        PageRequest pageRequest = PageRequest.of(
                page, size,
                Sort.Direction.fromString(sortDirection), sortBy);

        return ResponseEntity.ok(customerService.searchCustomers(
                name, email, phone, optedInTypes, pageRequest));
    }
}