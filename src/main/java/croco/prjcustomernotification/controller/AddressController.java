package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.AddressCreationDto;
import croco.prjcustomernotification.dto.AddressDto;
import croco.prjcustomernotification.enums.AddressType;
import croco.prjcustomernotification.service.interfaces.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getCustomerAddresses(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAddressesByCustomerId(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long customerId, @PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id, customerId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AddressDto>> getAddressesByType(@PathVariable Long customerId, @PathVariable AddressType type) {
        return ResponseEntity.ok(addressService.getAddressesByType(customerId, type));
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@PathVariable Long customerId, @Valid @RequestBody AddressCreationDto addressDto) {
        return new ResponseEntity<>(addressService.createAddress(customerId, addressDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long customerId, @PathVariable Long id, @Valid @RequestBody AddressCreationDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(id, customerId, addressDto));
    }

    @PutMapping("/{id}/primary")
    public ResponseEntity<AddressDto> setPrimaryAddress(@PathVariable Long customerId, @PathVariable Long id) {
        return ResponseEntity.ok(addressService.setPrimaryAddress(id, customerId));
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<AddressDto> updateVerificationStatus(@PathVariable Long customerId, @PathVariable Long id, @RequestParam boolean verified) {
        return ResponseEntity.ok(addressService.updateVerificationStatus(id, customerId, verified));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long customerId, @PathVariable Long id) {
        addressService.deleteAddress(id, customerId);
        return ResponseEntity.noContent().build();
    }
}