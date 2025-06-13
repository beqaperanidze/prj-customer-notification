package croco.prjcustomernotification.service.interfaces;

import croco.prjcustomernotification.dto.AddressCreationDto;
import croco.prjcustomernotification.dto.AddressDto;
import croco.prjcustomernotification.enums.AddressType;

import java.util.List;

public interface AddressService {
    List<AddressDto> getAddressesByCustomerId(Long customerId);

    AddressDto getAddressById(Long id, Long customerId);

    List<AddressDto> getAddressesByType(Long customerId, AddressType type);

    AddressDto createAddress(Long customerId, AddressCreationDto addressDto);

    AddressDto updateAddress(Long id, Long customerId, AddressCreationDto addressDto);

    AddressDto setPrimaryAddress(Long id, Long customerId);

    AddressDto updateVerificationStatus(Long id, Long customerId, boolean verified);

    void deleteAddress(Long id, Long customerId);
}