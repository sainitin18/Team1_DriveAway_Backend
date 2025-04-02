package com.DriveAway.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.DriveAway.project.model.Address;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserUserId(Long userId);
}
