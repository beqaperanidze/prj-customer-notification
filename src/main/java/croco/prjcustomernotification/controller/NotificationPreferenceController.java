package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.NotificationPreferenceCreationDto;
import croco.prjcustomernotification.dto.NotificationPreferenceDto;
import croco.prjcustomernotification.service.interfaces.NotificationPreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;

    /**
     * Retrieves all notification preferences for a customer.
     *
     * @param customerId The customer ID
     * @return List of notification preferences
     */
    @GetMapping
    public ResponseEntity<List<NotificationPreferenceDto>> getCustomerPreferences(@PathVariable Long customerId) {
        return ResponseEntity.ok(preferenceService.getPreferencesByCustomerId(customerId));
    }

    /**
     * Retrieves a specific notification preference.
     *
     * @param customerId The customer ID
     * @param id         The preference ID
     * @return The notification preference
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationPreferenceDto> getPreference(@PathVariable Long customerId, @PathVariable Long id) {
        return ResponseEntity.ok(preferenceService.getPreferenceById(id, customerId));
    }

    /**
     * Creates a new notification preference for a customer.
     *
     * @param customerId    The customer ID
     * @param preferenceDto The preference data
     * @return Created notification preference
     */
    @PostMapping
    public ResponseEntity<NotificationPreferenceDto> createPreference(@PathVariable Long customerId, @Valid @RequestBody NotificationPreferenceCreationDto preferenceDto) {
        return new ResponseEntity<>(preferenceService.createPreference(customerId, preferenceDto), HttpStatus.CREATED);
    }

    /**
     * Updates an existing notification preference.
     *
     * @param customerId    The customer ID
     * @param id            The preference ID
     * @param preferenceDto Updated preference data
     * @return Updated notification preference
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationPreferenceDto> updatePreference(@PathVariable Long customerId, @PathVariable Long id, @Valid @RequestBody NotificationPreferenceCreationDto preferenceDto) {
        return ResponseEntity.ok(preferenceService.updatePreference(id, customerId, preferenceDto));
    }

    /**
     * Deletes a notification preference.
     *
     * @param customerId The customer ID
     * @param id         The preference ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long customerId, @PathVariable Long id) {
        preferenceService.deletePreference(id, customerId);
        return ResponseEntity.noContent().build();
    }
}