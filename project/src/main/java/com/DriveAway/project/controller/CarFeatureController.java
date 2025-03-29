package com.DriveAway.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.service.CarFeatureService;

@RestController
@RequestMapping("/car-features")
public class CarFeatureController {

    @Autowired
    private CarFeatureService carFeatureService;

    // ✅ Add Features
    @PostMapping
    public ResponseEntity<CarFeatureDTO> addCarFeatures(@RequestBody CarFeatureDTO carFeatureDTO) {
        return ResponseEntity.ok(carFeatureService.addCarFeatures(carFeatureDTO));
    }

    // ✅ Update Features
    @PutMapping("/{carId}")
    public ResponseEntity<CarFeatureDTO> updateCarFeatures(@PathVariable Long carId, @RequestBody CarFeatureDTO carFeatureDTO) {
        return ResponseEntity.ok(carFeatureService.updateCarFeatures(carId, carFeatureDTO));
    }

    // ✅ Get Features
    @GetMapping("/{carId}")
    public ResponseEntity<CarFeatureDTO> getCarFeatures(@PathVariable Long carId) {
        return ResponseEntity.ok(carFeatureService.getCarFeatures(carId));
    }

    // ✅ Delete Features
    @DeleteMapping("/{carId}")
    public ResponseEntity<String> deleteCarFeatures(@PathVariable Long carId) {
        return ResponseEntity.ok(carFeatureService.deleteCarFeatures(carId));
    }
}
