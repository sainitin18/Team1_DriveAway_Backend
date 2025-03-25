package com.DriveAway.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DriveAway.project.model.Vehicle;



@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	
	   Optional<Vehicle> findByNumberPlate(String numberPlate);
}
