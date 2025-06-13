package croco.prjcustomernotification.repository;

import croco.prjcustomernotification.model.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {


    List<NotificationPreference> findByCustomerId(Long customerId);

    Optional<NotificationPreference> findByIdAndCustomerId(Long id, Long customerId);

    boolean existsByIdAndCustomerId(Long id, Long customerId);
}