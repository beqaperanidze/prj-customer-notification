package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.NotificationPreferenceCreationDto;
import croco.prjcustomernotification.dto.NotificationPreferenceDto;
import croco.prjcustomernotification.service.interfaces.NotificationPreferenceService;
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
@RequestMapping("/api/customers/{customerId}/preferences")
@RequiredArgsConstructor
@Tag(name = "Notification Preferences", description = "Operations related to customer notification preferences")
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;

    @GetMapping
    @Operation(summary = "Get all notification preferences", description = "Retrieves all notification preferences for a customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Preferences retrieved successfully"), @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)})
    public ResponseEntity<List<NotificationPreferenceDto>> getCustomerPreferences(@Parameter(description = "ID of the customer") @PathVariable Long customerId) {
        return ResponseEntity.ok(preferenceService.getPreferencesByCustomerId(customerId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific preference", description = "Retrieves a specific notification preference for a customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Preference found"), @ApiResponse(responseCode = "404", description = "Preference not found or doesn't belong to the customer", content = @Content)})
    public ResponseEntity<NotificationPreferenceDto> getPreference(@Parameter(description = "ID of the customer") @PathVariable Long customerId, @Parameter(description = "ID of the preference") @PathVariable Long id) {
        return ResponseEntity.ok(preferenceService.getPreferenceById(id, customerId));
    }

    @PostMapping
    @Operation(summary = "Create a notification preference", description = "Creates a new notification preference for a customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Preference created successfully"), @ApiResponse(responseCode = "400", description = "Invalid preference data", content = @Content), @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)})
    public ResponseEntity<NotificationPreferenceDto> createPreference(@Parameter(description = "ID of the customer") @PathVariable Long customerId, @Parameter(description = "Preference details") @Valid @RequestBody NotificationPreferenceCreationDto preferenceDto) {
        return new ResponseEntity<>(preferenceService.createPreference(customerId, preferenceDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a notification preference", description = "Updates an existing notification preference for a customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Preference updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid preference data", content = @Content), @ApiResponse(responseCode = "404", description = "Preference not found or doesn't belong to the customer", content = @Content)})
    public ResponseEntity<NotificationPreferenceDto> updatePreference(@Parameter(description = "ID of the customer") @PathVariable Long customerId, @Parameter(description = "ID of the preference") @PathVariable Long id, @Parameter(description = "Updated preference details") @Valid @RequestBody NotificationPreferenceCreationDto preferenceDto) {
        return ResponseEntity.ok(preferenceService.updatePreference(id, customerId, preferenceDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a notification preference", description = "Deletes a notification preference for a customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Preference deleted successfully"), @ApiResponse(responseCode = "404", description = "Preference not found or doesn't belong to the customer", content = @Content)})
    public ResponseEntity<Void> deletePreference(@Parameter(description = "ID of the customer") @PathVariable Long customerId, @Parameter(description = "ID of the preference") @PathVariable Long id) {
        preferenceService.deletePreference(id, customerId);
        return ResponseEntity.noContent().build();
    }
}