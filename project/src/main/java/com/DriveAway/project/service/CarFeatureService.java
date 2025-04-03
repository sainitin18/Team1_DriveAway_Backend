package com.DriveAway.project.service;

import com.DriveAway.project.dto.CarFeatureDTO;

public interface CarFeatureService {
    public CarFeatureDTO addCarFeatures(CarFeatureDTO carFeatureDTO);
    public CarFeatureDTO updateCarFeatures(Long carId, CarFeatureDTO carFeatureDTO);
    public CarFeatureDTO getCarFeatures(Long carId);
}
