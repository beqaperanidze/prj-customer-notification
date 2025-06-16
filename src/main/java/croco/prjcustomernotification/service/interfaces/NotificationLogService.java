package croco.prjcustomernotification.service.interfaces;

import croco.prjcustomernotification.dto.NotificationLogDto;
import croco.prjcustomernotification.enums.NotificationStatus;
import croco.prjcustomernotification.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

public interface NotificationLogService {

    NotificationLogDto getNotificationById(Long id);

    Page<NotificationLogDto> getNotificationsByCustomerId(Long customerId, Pageable pageable);

    Page<NotificationLogDto> searchNotifications(Long customerId, NotificationType type, NotificationStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Map<String, Object> getNotificationStatistics(LocalDateTime startDate, LocalDateTime endDate);

    Map<String, Object> generateCustomerOptInReport(LocalDateTime startDate, LocalDateTime endDate);

    NotificationLogDto logNotificationSent(Long customerId, NotificationType type, String subject, String content);

    NotificationLogDto updateNotificationStatus(Long id, NotificationStatus status, String failureReason);
}