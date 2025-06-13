package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.AddressCreationDto;
import croco.prjcustomernotification.dto.AddressDto;
import croco.prjcustomernotification.enums.AddressType;
import croco.prjcustomernotification.service.interfaces.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
@RequiredArgsConstructor
@Tag(name = "Address Management", description = "Operations related to customer addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    @Operation(summary = "Get all addresses for a customer", description = "Retrieves all addresses associated with the specified customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    public ResponseEntity<List<AddressDto>> getCustomerAddresses(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAddressesByCustomerId(customerId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific address", description = "Retrieves a specific address for a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address found"),
        @ApiResponse(responseCode = "404", description = "Address not found or doesn't belong to the customer", content = @Content)
    })
    public ResponseEntity<AddressDto> getAddress(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId,
            @Parameter(description = "ID of the address") @PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id, customerId));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get addresses by type", description = "Retrieves all addresses of a specific type for a customer")
    public ResponseEntity<List<AddressDto>> getAddressesByType(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId,
            @Parameter(description = "Address type (e.g., HOME, BUSINESS)") @PathVariable AddressType type) {
        return ResponseEntity.ok(addressService.getAddressesByType(customerId, type));
    }

    @PostMapping
    @Operation(summary = "Create a new address", description = "Creates a new address for a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Address created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid address data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    public ResponseEntity<AddressDto> createAddress(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId,
            @Parameter(description = "Address details") @Valid @RequestBody AddressCreationDto addressDto) {
        return new ResponseEntity<>(addressService.createAddress(customerId, addressDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an address", description = "Updates an existing address for a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid address data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Address not found or doesn't belong to the customer", content = @Content)
    })
    public ResponseEntity<AddressDto> updateAddress(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId,
            @Parameter(description = "ID of the address") @PathVariable Long id,
            @Parameter(description = "Updated address details") @Valid @RequestBody AddressCreationDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(id, customerId, addressDto));
    }

    @PutMapping("/{id}/primary")
    @Operation(summary = "Set address as primary", description = "Sets an address as the customer's primary address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Address set as primary successfully"),
        @ApiResponse(responseCode = "404", description = "Address not found or doesn't belong to the customer", content = @Content)
    })
    public ResponseEntity<AddressDto> setPrimaryAddress(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId,
            @Parameter(description = "ID of the address") @PathVariable Long id) {
        return ResponseEntity.ok(addressService.setPrimaryAddress(id, customerId));
    }

    @PutMapping("/{id}/verify")
    @Operation(summary = "Update address verification", description = "Updates the verification status of an address")
    public ResponseEntity<AddressDto> updateVerificationStatus(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId,
            @Parameter(description = "ID of the address") @PathVariable Long id,
            @Parameter(description = "Verification status (true/false)") @RequestParam boolean verified) {
        return ResponseEntity.ok(addressService.updateVerificationStatus(id, customerId, verified));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an address", description = "Deletes an address for a customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Address not found or doesn't belong to the customer", content = @Content)
    })
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "ID of the customer") @PathVariable Long customerId,
            @Parameter(description = "ID of the address") @PathVariable Long id) {
        addressService.deleteAddress(id, customerId);
        return ResponseEntity.noContent().build();
    }
}