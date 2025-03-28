package com.DriveAway.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DriveAway.project.model.CarFeatures;

import java.util.Optional;

public interface CarFeatureRepository extends JpaRepository<CarFeatures, Integer> {
    Optional<CarFeatures> findByCar_CarId(int carId);
}

