package croco.prjcustomernotification.repository;

import croco.prjcustomernotification.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
