package croco.prjcustomernotification.repository;

import croco.prjcustomernotification.enums.NotificationStatus;
import croco.prjcustomernotification.enums.NotificationType;
import croco.prjcustomernotification.model.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long>, JpaSpecificationExecutor<NotificationLog> {

    @Query("SELECT n.status as status, COUNT(n) as count FROM NotificationLog n " +
            "WHERE (CAST(:startDate AS timestamp) IS NULL OR n.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR n.createdAt <= :endDate) " +
            "GROUP BY n.status")
    Map<NotificationStatus, Long> countByStatusGrouped(@Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);

    @Query("SELECT n.type as type, n.status as status, COUNT(n) as count FROM NotificationLog n " +
            "WHERE (CAST(:startDate AS timestamp) IS NULL OR n.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR n.createdAt <= :endDate) " +
            "GROUP BY n.type, n.status")
    Map<NotificationType, Map<NotificationStatus, Long>> countByTypeAndStatusGrouped(@Param("startDate") LocalDateTime startDate,
                                                                                     @Param("endDate") LocalDateTime endDate);


    @Query("SELECT CAST(n.createdAt as date) as date, " +
            "COUNT(n) as total, " +
            "SUM(CASE WHEN n.status = 'DELIVERED' THEN 1 ELSE 0 END) as delivered, " +
            "SUM(CASE WHEN n.status = 'FAILED' THEN 1 ELSE 0 END) as failed " +
            "FROM NotificationLog n " +
            "WHERE n.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY CAST(n.createdAt as date) " +
            "ORDER BY CAST(n.createdAt as date)")
    List<Map<String, Object>> countByDayInRange(@Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT n.type as type, COUNT(DISTINCT n.customer.id) as count " +
            "FROM NotificationLog n " +
            "WHERE (CAST(:startDate AS timestamp) IS NULL OR n.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR n.createdAt <= :endDate) " +
            "GROUP BY n.type")
    Map<NotificationType, Long> countCustomersByNotificationType(@Param("startDate") LocalDateTime startDate,
                                                                 @Param("endDate") LocalDateTime endDate);

    @Query("SELECT n.type as type, " +
            "SUM(CASE WHEN n.status = 'DELIVERED' THEN 1 ELSE 0 END) * 100.0 / COUNT(n) as successRate " +
            "FROM NotificationLog n " +
            "WHERE (CAST(:startDate AS timestamp) IS NULL OR n.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR n.createdAt <= :endDate) " +
            "GROUP BY n.type")
    Map<NotificationType, Double> getSuccessRateByType(@Param("startDate") LocalDateTime startDate,
                                                       @Param("endDate") LocalDateTime endDate);


    @Query(value = "SELECT failures.type::text as type_key, " +
            "jsonb_agg(json_build_object('reason', failures.reason, 'count', failures.count)) as reasons " +
            "FROM (SELECT n.type as type, n.failure_reason as reason, COUNT(n.id) as count " +
            "      FROM notification_logs n " +
            "      WHERE n.status = 'FAILED' " +
            "      AND (n.created_at >= COALESCE(:startDate, '-infinity'::timestamp)) " +
            "      AND (n.created_at <= COALESCE(:endDate, 'infinity'::timestamp)) " +
            "      GROUP BY n.type, n.failure_reason " +
            "      ORDER BY n.type, COUNT(n.id) DESC) as failures " +
            "GROUP BY failures.type",
            nativeQuery = true)
    List<Map<String, Object>> getTopFailureReasonsByType(@Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT n.type as type, " +
            "COUNT(n) as deliveredCount, " +
            "0.0 as engagementRate " +
            "FROM NotificationLog n " +
            "WHERE (CAST(:startDate AS timestamp) IS NULL OR n.createdAt >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR n.createdAt <= :endDate) " +
            "AND n.status = 'DELIVERED' " +
            "GROUP BY n.type")
    Map<NotificationType, Double> getEngagementRateByType(@Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);

    Page<NotificationLog> findByCustomerId(Long customerId, Pageable pageable);

}