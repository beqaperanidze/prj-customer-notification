package croco.prjcustomernotification.service.interfaces;

import croco.prjcustomernotification.dto.NotificationPreferenceCreationDto;
import croco.prjcustomernotification.dto.NotificationPreferenceDto;

import java.util.List;


public interface NotificationPreferenceService {
    List<NotificationPreferenceDto> getPreferencesByCustomerId(Long customerId);

    NotificationPreferenceDto getPreferenceById(Long id, Long customerId);

    NotificationPreferenceDto createPreference(Long customerId, NotificationPreferenceCreationDto preferenceDto);

    NotificationPreferenceDto updatePreference(Long id, Long customerId, NotificationPreferenceCreationDto preferenceDto);

    void deletePreference(Long id, Long customerId);
}