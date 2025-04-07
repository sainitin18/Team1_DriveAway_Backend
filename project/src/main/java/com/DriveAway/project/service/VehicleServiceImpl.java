package com.DriveAway.project.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.exception.VehicleAlreadyExistsException;
import com.DriveAway.project.exception.VehicleNotFoundException;
import com.DriveAway.project.model.Image;
import com.DriveAway.project.model.Vehicle;
import com.DriveAway.project.repository.ImageRepository;
import com.DriveAway.project.repository.VehicleRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private Cloudinary cloudinary;
    
    @Override
    public Vehicle addVehicle(VehicleDTO vehicleDTO, MultipartFile[] images ) {
        // ✅ Check if vehicle with the same number plate already exists
        Optional<Vehicle> existingVehicle = vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (existingVehicle.isPresent()) {
            throw new VehicleAlreadyExistsException("Vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setYear(vehicleDTO.getYear());
        vehicle.setFuelType(vehicleDTO.getFuelType());
        vehicle.setTransmission(vehicleDTO.getTransmission());
        vehicle.setNumberPlate(vehicleDTO.getNumberPlate());
        vehicle.setPrice(vehicleDTO.getPrice());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setColor(vehicleDTO.getColor());
        vehicle.setSeater(vehicleDTO.getSeater());
        vehicle.setSecurityAmount(vehicleDTO.getSecurityAmount());

        vehicle = vehicleRepository.save(vehicle); // Save first to get vehicle ID
        if (images != null) {
            for (MultipartFile file : images) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("secure_url").toString();

                    Image image = new Image();
                    image.setUrl(imageUrl);
                    image.setVehicle(vehicle);
                    imageRepository.save(image);
                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed: " + file.getOriginalFilename(), e);
                }
            }
        }

        return vehicle;

//        if (vehicleDTO.getImageUrls() != null) {
//            for (String url : vehicleDTO.getImageUrls()) {
//                Image image = new Image();
//                image.setUrl(url);
//                image.setVehicle(vehicle);
//                imageRepository.save(image);
//            }
//        }
//
//        return vehicle;

    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleById(Long carId) {
        return vehicleRepository.findById(carId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + carId));
    }

//    @Override
//    public Vehicle updateVehicle(Long carId, VehicleDTO vehicleDTO) {
//        Vehicle existingVehicle = vehicleRepository.findById(carId)
//                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + carId));
//
//        // ✅ Check if another vehicle with the same number plate exists (excluding the current one)
//        Optional<Vehicle> vehicleWithSameNumberPlate = vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate());
//        if (vehicleWithSameNumberPlate.isPresent() && !vehicleWithSameNumberPlate.get().getCarId().equals(carId)) {
//            throw new VehicleAlreadyExistsException("Another vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists.");
//        }
//
//        existingVehicle.setModel(vehicleDTO.getModel());
//        existingVehicle.setBrand(vehicleDTO.getBrand());
//        existingVehicle.setType(vehicleDTO.getType());
//        existingVehicle.setYear(vehicleDTO.getYear());
//        existingVehicle.setFuelType(vehicleDTO.getFuelType());
//        existingVehicle.setTransmission(vehicleDTO.getTransmission());
//        existingVehicle.setNumberPlate(vehicleDTO.getNumberPlate());
//        existingVehicle.setPrice(vehicleDTO.getPrice());
//        existingVehicle.setStatus(vehicleDTO.getStatus());
//        existingVehicle.setColor(vehicleDTO.getColor());
//        existingVehicle.setSeater(vehicleDTO.getSeater());
//        existingVehicle.setSecurityAmount(vehicleDTO.getSecurityAmount());
//
//        existingVehicle = vehicleRepository.save(existingVehicle);
//
//        // Optionally delete old images here if needed
//
//        if (vehicleDTO.getImageUrls() != null) {
//            for (String url : vehicleDTO.getImageUrls()) {
//                Image image = new Image();
//                image.setUrl(url);
//                image.setVehicle(existingVehicle);
//                imageRepository.save(image);
//            }
//        }
//
//        return existingVehicle;
//    }
    @Override
    public Vehicle updateVehicle(Long carId, VehicleDTO vehicleDTO, MultipartFile[] images) {
        Vehicle existingVehicle = vehicleRepository.findById(carId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + carId));

        Optional<Vehicle> vehicleWithSameNumberPlate = vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (vehicleWithSameNumberPlate.isPresent() && !vehicleWithSameNumberPlate.get().getCarId().equals(carId)) {
            throw new VehicleAlreadyExistsException("Another vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists.");
        }

        // Update vehicle fields
        existingVehicle.setModel(vehicleDTO.getModel());
        existingVehicle.setBrand(vehicleDTO.getBrand());
        existingVehicle.setType(vehicleDTO.getType());
        existingVehicle.setYear(vehicleDTO.getYear());
        existingVehicle.setFuelType(vehicleDTO.getFuelType());
        existingVehicle.setTransmission(vehicleDTO.getTransmission());
        existingVehicle.setNumberPlate(vehicleDTO.getNumberPlate());
        existingVehicle.setPrice(vehicleDTO.getPrice());
        existingVehicle.setStatus(vehicleDTO.getStatus());
        existingVehicle.setColor(vehicleDTO.getColor());
        existingVehicle.setSeater(vehicleDTO.getSeater());
        existingVehicle.setSecurityAmount(vehicleDTO.getSecurityAmount());

        existingVehicle = vehicleRepository.save(existingVehicle);

        // Optional: Remove old images if needed
        // imageRepository.deleteByVehicle(existingVehicle);

        // Save new images
        if (images != null) {
            for (MultipartFile file : images) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("secure_url").toString();

                    Image image = new Image();
                    image.setUrl(imageUrl);
                    image.setVehicle(existingVehicle);
                    imageRepository.save(image);
                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed: " + file.getOriginalFilename(), e);
                }
            }
        }

        return existingVehicle;
    }

    @Override
    public void deleteVehicle(Long carId) {
        if (!vehicleRepository.existsById(carId)) {
            throw new VehicleNotFoundException("Vehicle not found with ID: " + carId);
        }
        vehicleRepository.deleteById(carId);
    }
}
