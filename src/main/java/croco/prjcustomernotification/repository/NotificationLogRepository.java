package croco.prjcustomernotification.repository;

import croco.prjcustomernotification.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}
