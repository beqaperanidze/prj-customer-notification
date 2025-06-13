package croco.prjcustomernotification.repository;

import croco.prjcustomernotification.enums.AddressType;
import croco.prjcustomernotification.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {


    List<Address> findByCustomerId(Long customerId);

    List<Address> findByCustomerIdAndType(Long customerId, AddressType type);

    Optional<Address> findByIdAndCustomerId(Long id, Long customerId);

    List<Address> findByCustomerIdAndTypeAndPrimaryTrue(Long customerId, AddressType type);

    boolean existsByIdAndCustomerId(Long id, Long customerId);
}