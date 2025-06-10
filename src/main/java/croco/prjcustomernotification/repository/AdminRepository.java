package croco.prjcustomernotification.repository;

import croco.prjcustomernotification.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {
}
