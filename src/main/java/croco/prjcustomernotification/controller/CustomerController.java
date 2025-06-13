package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.CustomerCreationDto;
import croco.prjcustomernotification.dto.CustomerDto;
import croco.prjcustomernotification.dto.CustomerPageResponseDto;
import croco.prjcustomernotification.enums.NotificationType;
import croco.prjcustomernotification.service.interfaces.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Customer Management", description = "Operations related to customer records")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Get all customers", description = "Retrieves a list of all customers in the system")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Retrieves a specific customer by their unique identifier")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Customer found"), @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)})
    public ResponseEntity<CustomerDto> getCustomer(@Parameter(description = "ID of the customer") @PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer record in the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Customer created successfully"), @ApiResponse(responseCode = "400", description = "Invalid customer data", content = @Content)})
    public ResponseEntity<CustomerDto> createCustomer(@Parameter(description = "Customer details") @Valid @RequestBody CustomerCreationDto customerDto) {
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a customer", description = "Updates an existing customer's information")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Customer updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid customer data", content = @Content), @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)})
    public ResponseEntity<CustomerDto> updateCustomer(@Parameter(description = "ID of the customer") @PathVariable Long id, @Parameter(description = "Updated customer details") @Valid @RequestBody CustomerCreationDto customerDto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer", description = "Removes a customer record from the system")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Customer deleted successfully"), @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)})
    public ResponseEntity<Void> deleteCustomer(@Parameter(description = "ID of the customer") @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers", description = "Searches for customers with filtering, pagination and sorting options")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public ResponseEntity<CustomerPageResponseDto> searchCustomers(@Parameter(description = "Customer name filter") @RequestParam(required = false) String name, @Parameter(description = "Customer email filter") @RequestParam(required = false) String email, @Parameter(description = "Customer phone filter") @RequestParam(required = false) String phone, @Parameter(description = "Filter by notification preferences") @RequestParam(required = false) Set<NotificationType> optedInTypes, @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page, @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size, @Parameter(description = "Field to sort by") @RequestParam(defaultValue = "id") String sortBy, @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDirection) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortBy);

        return ResponseEntity.ok(customerService.searchCustomers(name, email, phone, optedInTypes, pageRequest));
    }
}