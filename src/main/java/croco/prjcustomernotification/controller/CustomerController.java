package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.CustomerCreationDto;
import croco.prjcustomernotification.dto.CustomerDto;
import croco.prjcustomernotification.service.interfaces.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}