package com.DriveAway.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.DriveAway.project.model.CarFeature;
import java.util.Optional;

@Repository
public interface CarFeatureRepository extends JpaRepository<CarFeature, Long> {
    Optional<CarFeature> findByVehicle_CarId(Long carId);
}

