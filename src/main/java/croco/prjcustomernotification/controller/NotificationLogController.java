package croco.prjcustomernotification.controller;

import croco.prjcustomernotification.dto.NotificationLogDto;
import croco.prjcustomernotification.enums.NotificationStatus;
import croco.prjcustomernotification.enums.NotificationType;
import croco.prjcustomernotification.service.interfaces.NotificationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationLogController {

    private final NotificationLogService notificationLogService;

    /**
     * Gets a notification by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationLogDto> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationLogService.getNotificationById(id));
    }

    /**
     * Search notifications with filters
     */
    @GetMapping("/search")
    public ResponseEntity<Page<NotificationLogDto>> searchNotifications(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) NotificationStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(notificationLogService.searchNotifications(customerId, type, status, startDate, endDate, pageable));
    }

    /**
     * Gets notification statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getNotificationStatistics(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        return ResponseEntity.ok(notificationLogService.getNotificationStatistics(startDate, endDate));
    }

    /**
     * Updates notification status (e.g., for webhook callbacks)
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<NotificationLogDto> updateNotificationStatus(
            @PathVariable Long id,
            @RequestParam NotificationStatus status,
            @RequestParam(required = false) String failureReason) {
        return ResponseEntity.ok(notificationLogService.updateNotificationStatus(id, status, failureReason));
    }
}