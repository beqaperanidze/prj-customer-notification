package croco.prjcustomernotification.service.implementation;

import croco.prjcustomernotification.dto.NotificationPreferenceCreationDto;
import croco.prjcustomernotification.dto.NotificationPreferenceDto;
import croco.prjcustomernotification.exception.CustomerNotFoundException;
import croco.prjcustomernotification.exception.NotificationPreferenceNotFoundException;
import croco.prjcustomernotification.model.Customer;
import croco.prjcustomernotification.model.NotificationPreference;
import croco.prjcustomernotification.repository.CustomerRepository;
import croco.prjcustomernotification.repository.NotificationPreferenceRepository;
import croco.prjcustomernotification.service.interfaces.NotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private final NotificationPreferenceRepository preferenceRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<NotificationPreferenceDto> getPreferencesByCustomerId(Long customerId) {
        validateCustomerExists(customerId);
        return preferenceRepository.findByCustomerId(customerId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public NotificationPreferenceDto getPreferenceById(Long id, Long customerId) {
        validateCustomerExists(customerId);
        NotificationPreference preference = preferenceRepository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new NotificationPreferenceNotFoundException("Notification preference not found with id: " + id));
        return mapToDto(preference);
    }

    @Override
    @Transactional
    public NotificationPreferenceDto createPreference(Long customerId, NotificationPreferenceCreationDto preferenceDto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

        NotificationPreference preference = new NotificationPreference();
        preference.setType(preferenceDto.getType());
        preference.setChannelType(preferenceDto.getChannelType());
        preference.setOptedIn(preferenceDto.isOptedIn());
        preference.setCustomer(customer);

        NotificationPreference savedPreference = preferenceRepository.save(preference);
        return mapToDto(savedPreference);
    }

    @Override
    @Transactional
    public NotificationPreferenceDto updatePreference(Long id, Long customerId, NotificationPreferenceCreationDto preferenceDto) {
        validateCustomerExists(customerId);
        NotificationPreference preference = preferenceRepository.findByIdAndCustomerId(id, customerId).orElseThrow(() -> new NotificationPreferenceNotFoundException("Notification preference not found with id: " + id));

        preference.setType(preferenceDto.getType());
        preference.setChannelType(preferenceDto.getChannelType());
        preference.setOptedIn(preferenceDto.isOptedIn());

        NotificationPreference updatedPreference = preferenceRepository.save(preference);
        return mapToDto(updatedPreference);
    }

    @Override
    @Transactional
    public void deletePreference(Long id, Long customerId) {
        validateCustomerExists(customerId);
        if (!preferenceRepository.existsByIdAndCustomerId(id, customerId)) {
            throw new NotificationPreferenceNotFoundException("Notification preference not found with id: " + id);
        }
        preferenceRepository.deleteById(id);
    }

    private void validateCustomerExists(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with id: " + customerId);
        }
    }

    private NotificationPreferenceDto mapToDto(NotificationPreference preference) {
        return NotificationPreferenceDto.builder().id(preference.getId()).type(preference.getType()).channelType(preference.getChannelType()).optedIn(preference.isOptedIn()).customerId(preference.getCustomer().getId()).build();
    }
}