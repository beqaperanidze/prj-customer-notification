package croco.prjcustomernotification.service.implementation;

import croco.prjcustomernotification.dto.NotificationLogDto;
import croco.prjcustomernotification.enums.NotificationStatus;
import croco.prjcustomernotification.enums.NotificationType;
import croco.prjcustomernotification.exception.ResourceNotFoundException;
import croco.prjcustomernotification.model.Address;
import croco.prjcustomernotification.model.Customer;
import croco.prjcustomernotification.model.NotificationLog;
import croco.prjcustomernotification.repository.CustomerRepository;
import croco.prjcustomernotification.repository.NotificationLogRepository;
import croco.prjcustomernotification.service.interfaces.NotificationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationLogServiceImpl implements NotificationLogService {

    private final NotificationLogRepository notificationLogRepository;
    private final CustomerRepository customerRepository;

    @Override
    public NotificationLogDto getNotificationById(Long id) {
        NotificationLog log = notificationLogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        return mapToDto(log);
    }

    @Override
    public Page<NotificationLogDto> getNotificationsByCustomerId(Long customerId, Pageable pageable) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        Page<NotificationLog> notificationsPage = notificationLogRepository.findByCustomerId(customerId, pageable);

        return notificationsPage.map(this::mapToDto);
    }

    @Override
    public Page<NotificationLogDto> searchNotifications(Long customerId, NotificationType type, NotificationStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {

        Specification<NotificationLog> spec = Specification.allOf();

        if (customerId != null) {
            spec = spec.and((root, _, cb) -> cb.equal(root.get("customer").get("id"), customerId));
        }

        if (type != null) {
            spec = spec.and((root, _, cb) -> cb.equal(root.get("type"), type));
        }

        if (status != null) {
            spec = spec.and((root, _, cb) -> cb.equal(root.get("status"), status));
        }

        if (startDate != null) {
            spec = spec.and((root, _, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, _, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
        }

        return notificationLogRepository.findAll(spec, pageable).map(this::mapToDto);
    }

    @Override
    public Map<String, Object> getNotificationStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> stats = new HashMap<>();

        Map<NotificationStatus, Long> statusCounts = notificationLogRepository.countByStatusGrouped(startDate, endDate);
        stats.put("statusCounts", statusCounts);

        Map<NotificationType, Map<NotificationStatus, Long>> typeStats = notificationLogRepository.countByTypeAndStatusGrouped(startDate, endDate);
        stats.put("typeStatistics", typeStats);

        List<Map<String, Object>> dailyCounts = notificationLogRepository.countByDayInRange(startDate, endDate);
        stats.put("dailyStatistics", dailyCounts);

        return stats;
    }

    @Override
    public Map<String, Object> generateCustomerOptInReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();

        Map<NotificationType, Long> optInCountsByType = notificationLogRepository.countCustomersByNotificationType(startDate, endDate);
        report.put("optInCountsByType", optInCountsByType);

        Map<NotificationType, Double> successRateByType = notificationLogRepository.getSuccessRateByType(startDate, endDate);
        report.put("successRateByType", successRateByType);

        List<Map<String, Object>> failureReasonsList = notificationLogRepository.getTopFailureReasonsByType(startDate, endDate);
        Map<NotificationType, List<Map<String, Object>>> topFailureReasons = new HashMap<>();

        for (Map<String, Object> item : failureReasonsList) {
            String typeKey = (String) item.get("type_key");
            NotificationType notificationType = NotificationType.valueOf(typeKey);
            @SuppressWarnings("unchecked") List<Map<String, Object>> reasons = (List<Map<String, Object>>) item.get("reasons");
            topFailureReasons.put(notificationType, reasons);
        }

        report.put("topFailureReasons", topFailureReasons);

        Map<NotificationType, Double> engagementRateByType = notificationLogRepository.getEngagementRateByType(startDate, endDate);
        report.put("engagementRateByType", engagementRateByType);

        return report;
    }

    @Override
    @Transactional
    public NotificationLogDto logNotificationSent(Long customerId, NotificationType type, String subject, String content) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<Address> addresses = new ArrayList<>(customer.getAddresses());

        Address address = addresses.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No suitable address found for this notification type"));

        NotificationLog log = NotificationLog.builder()
                .customer(customer)
                .address(address)
                .type(type)
                .status(NotificationStatus.PENDING)
                .subject(subject)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();

        NotificationLog savedLog = notificationLogRepository.save(log);

        return mapToDto(savedLog);
    }

    @Override
    @Transactional
    public NotificationLogDto updateNotificationStatus(Long id, NotificationStatus status, String failureReason) {
        NotificationLog log = notificationLogRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        log.setStatus(status);

        if (status == NotificationStatus.DELIVERED && log.getDeliveredAt() == null) {
            log.setDeliveredAt(LocalDateTime.now());
        }

        if (status == NotificationStatus.FAILED) {
            log.setFailureReason(failureReason);
        }

        return mapToDto(notificationLogRepository.save(log));
    }

    private NotificationLogDto mapToDto(NotificationLog log) {
        return NotificationLogDto.builder().id(log.getId()).customerId(log.getCustomer().getId()).customerName(log.getCustomer().getFirstName() + " " + log.getCustomer().getLastName()).addressId(log.getAddress().getId()).addressValue(log.getAddress().getValue()).type(log.getType()).status(log.getStatus()).externalReferenceId(log.getExternalReferenceId()).subject(log.getSubject()).content(log.getContent()).sentAt(log.getSentAt()).deliveredAt(log.getDeliveredAt()).failureReason(log.getFailureReason()).createdAt(log.getCreatedAt()).updatedAt(log.getUpdatedAt()).build();
    }

}