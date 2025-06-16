package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.NotificationLogDto;
import croco.prjcustomernotification.enums.NotificationStatus;
import croco.prjcustomernotification.enums.NotificationType;
import croco.prjcustomernotification.service.interfaces.NotificationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Logs", description = "Operations related to notification tracking and statistics")
public class NotificationLogController {

    private final NotificationLogService notificationLogService;

    // placeholder method, this should be a part of different microservice
    @PostMapping("/send")
    @Operation(summary = "Send a new notification", description = "Sends a new notification to a customer and logs the attempt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Notification accepted for processing"),
            @ApiResponse(responseCode = "400", description = "Invalid notification request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    public ResponseEntity<NotificationLogDto> sendNotification(
            @Parameter(description = "Customer ID to send notification to") @RequestParam Long customerId,
            @Parameter(description = "Type of notification to send") @RequestParam NotificationType type,
            @Parameter(description = "Subject of the notification") @RequestParam String subject,
            @Parameter(description = "Content of the notification") @RequestParam String content) {


        NotificationLogDto notification = notificationLogService.logNotificationSent(customerId, type, subject, content);

        return ResponseEntity.accepted().body(notification);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Retrieves a specific notification by its unique identifier")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Notification found"), @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)})
    public ResponseEntity<NotificationLogDto> getNotificationById(@Parameter(description = "ID of the notification") @PathVariable Long id) {
        return ResponseEntity.ok(notificationLogService.getNotificationById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get notifications by customer ID", description = "Retrieves all notifications for a specific customer with pagination")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"), @ApiResponse(responseCode = "400", description = "Invalid customer ID", content = @Content)})
    public ResponseEntity<Page<NotificationLogDto>> getNotificationsByCustomerId(@Parameter(description = "ID of the customer") @PathVariable Long customerId, @Parameter(description = "Pagination and sorting parameters") Pageable pageable) {
        return ResponseEntity.ok(notificationLogService.getNotificationsByCustomerId(customerId, pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Search notifications", description = "Searches notifications with various filters and pagination")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public ResponseEntity<Page<NotificationLogDto>> searchNotifications(@Parameter(description = "Filter by customer ID") @RequestParam(required = false) Long customerId, @Parameter(description = "Filter by notification type") @RequestParam(required = false) NotificationType type, @Parameter(description = "Filter by notification status") @RequestParam(required = false) NotificationStatus status, @Parameter(description = "Filter by start date") @RequestParam(required = false) LocalDateTime startDate, @Parameter(description = "Filter by end date") @RequestParam(required = false) LocalDateTime endDate, @Parameter(description = "Pagination and sorting parameters") Pageable pageable) {
        return ResponseEntity.ok(notificationLogService.searchNotifications(customerId, type, status, startDate, endDate, pageable));
    }

    @GetMapping("/customer/{customerId}/stats")
    @Operation(summary = "Get notification statistics for a customer", description = "Retrieves statistics about notifications for a specific customer within a given date range")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getCustomerNotificationStatistics(@Parameter(description = "ID of the customer") @PathVariable Long customerId, @Parameter(description = "Start date for statistics (format: yyyy-MM-ddTHH:mm:ss)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @Parameter(description = "End date for statistics (format: yyyy-MM-ddTHH:mm:ss)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> stats = notificationLogService.getNotificationStatistics(startDate, endDate);

        stats.put("customerId", customerId);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get notification statistics", description = "Retrieves statistics about notifications within a given date range")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<Map<String, Object>> getNotificationStatistics(@Parameter(description = "Start date for statistics (format: yyyy-MM-ddTHH:mm:ss)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

                                                                         @Parameter(description = "End date for statistics (format: yyyy-MM-ddTHH:mm:ss)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return ResponseEntity.ok(notificationLogService.getNotificationStatistics(startDate, endDate));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update notification status", description = "Updates the status of a notification, often used for webhook callbacks")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Status updated successfully"), @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content)})
    public ResponseEntity<NotificationLogDto> updateNotificationStatus(@Parameter(description = "ID of the notification") @PathVariable Long id, @Parameter(description = "New notification status") @RequestParam NotificationStatus status, @Parameter(description = "Reason for failure (if applicable)") @RequestParam(required = false) String failureReason) {
        return ResponseEntity.ok(notificationLogService.updateNotificationStatus(id, status, failureReason));
    }

    @GetMapping("/opt-in-report")
    @Operation(summary = "Generate customer opt-in report",
            description = "Retrieves statistics about customer opt-in rates and notification success rates within a given date range")
    @ApiResponse(responseCode = "200", description = "Report generated successfully")
    public ResponseEntity<Map<String, Object>> generateCustomerOptInReport(
            @Parameter(description = "Start date for report (format: yyyy-MM-ddTHH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @Parameter(description = "End date for report (format: yyyy-MM-ddTHH:mm:ss)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {

        return ResponseEntity.ok(notificationLogService.generateCustomerOptInReport(startDate, endDate));
    }
}