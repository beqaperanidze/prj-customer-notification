package croco.prjcustomernotification.service.implementation;

import croco.prjcustomernotification.dto.AddressCreationDto;
import croco.prjcustomernotification.dto.AddressDto;
import croco.prjcustomernotification.enums.AddressType;
import croco.prjcustomernotification.exception.ResourceNotFoundException;
import croco.prjcustomernotification.model.Address;
import croco.prjcustomernotification.model.Customer;
import croco.prjcustomernotification.repository.AddressRepository;
import croco.prjcustomernotification.repository.CustomerRepository;
import croco.prjcustomernotification.service.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<AddressDto> getAddressesByCustomerId(Long customerId) {
        validateCustomerExists(customerId);
        return addressRepository.findByCustomerId(customerId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AddressDto getAddressById(Long id, Long customerId) {
        validateCustomerExists(customerId);
        Address address = addressRepository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return mapToDto(address);
    }

    @Override
    public List<AddressDto> getAddressesByType(Long customerId, AddressType type) {
        validateCustomerExists(customerId);
        return addressRepository.findByCustomerIdAndType(customerId, type).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto createAddress(Long customerId, AddressCreationDto addressDto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        if (addressDto.isPrimary()) {
            resetPrimaryAddresses(customerId, addressDto.getType());
        }

        Address address = Address.builder().type(addressDto.getType()).value(addressDto.getValue()).customer(customer).primary(addressDto.isPrimary()).verified(false).build();

        Address savedAddress = addressRepository.save(address);
        return mapToDto(savedAddress);
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long id, Long customerId, AddressCreationDto addressDto) {
        validateCustomerExists(customerId);
        Address address = addressRepository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        if (addressDto.isPrimary() && !address.isPrimary()) {
            resetPrimaryAddresses(customerId, addressDto.getType());
        }

        address.setType(addressDto.getType());
        address.setValue(addressDto.getValue());
        address.setPrimary(addressDto.isPrimary());

        Address updatedAddress = addressRepository.save(address);
        return mapToDto(updatedAddress);
    }

    @Override
    @Transactional
    public AddressDto setPrimaryAddress(Long id, Long customerId) {
        validateCustomerExists(customerId);
        Address address = addressRepository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        if (!address.isPrimary()) {
            resetPrimaryAddresses(customerId, address.getType());
            address.setPrimary(true);
            address = addressRepository.save(address);
        }

        return mapToDto(address);
    }

    @Override
    @Transactional
    public AddressDto updateVerificationStatus(Long id, Long customerId, boolean verified) {
        validateCustomerExists(customerId);
        Address address = addressRepository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        address.setVerified(verified);
        Address updatedAddress = addressRepository.save(address);
        return mapToDto(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id, Long customerId) {
        validateCustomerExists(customerId);
        if (!addressRepository.existsByIdAndCustomerId(id, customerId)) {
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
    }

    private void resetPrimaryAddresses(Long customerId, AddressType type) {
        List<Address> primaryAddresses = addressRepository.findByCustomerIdAndTypeAndPrimaryTrue(customerId, type);
        for (Address address : primaryAddresses) {
            address.setPrimary(false);
            addressRepository.save(address);
        }
    }

    private void validateCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
    }

    private AddressDto mapToDto(Address address) {
        return AddressDto.builder().id(address.getId()).type(address.getType()).value(address.getValue()).customerId(address.getCustomer().getId()).verified(address.isVerified()).primary(address.isPrimary()).createdAt(address.getCreatedAt()).updatedAt(address.getUpdatedAt()).build();
    }
}