package com.DriveAway.project.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.dto.VehicleDTO;
import com.DriveAway.project.exception.VehicleAlreadyExistsException;
import com.DriveAway.project.exception.VehicleNotFoundException;
import com.DriveAway.project.model.CarFeature;
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
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public Vehicle addVehicle(VehicleDTO vehicleDTO, MultipartFile[] images, CarFeatureDTO featureDTO) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (existingVehicle.isPresent()) {
            throw new VehicleAlreadyExistsException("Vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists.");
        }

        Vehicle vehicle = modelMapper.map(vehicleDTO, Vehicle.class);
        System.out.println("Feature DTO: " + featureDTO);
        if (featureDTO != null) {
            CarFeature carFeature = modelMapper.map(featureDTO, CarFeature.class);
            carFeature.setVehicle(vehicle);         
            vehicle.setCarFeature(carFeature);      
        }

        vehicle = vehicleRepository.save(vehicle); 

        // Save images
        if (images != null) {
        	List<Image> imagesList = new ArrayList<Image>();
            for (MultipartFile file : images) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("secure_url").toString();
                    Image image = new Image();
                    image.setUrl(imageUrl);
                    image.setVehicle(vehicle);
                    imagesList.add(image);
                    imageRepository.save(image);
                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed: " + file.getOriginalFilename(), e);
                }
            }
            vehicle.setImages(imagesList);
        }
        return vehicle;
    }

    
    private CarFeature convertToCarFeatureEntity(CarFeatureDTO dto, Vehicle vehicle) {
        CarFeature feature = modelMapper.map(dto, CarFeature.class);
        feature.setVehicle(vehicle);
        return feature;
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
    
    @Override
    public int getAvailableVehicleCount() {
        return vehicleRepository.countByStatus("AVAILABLE");
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
//    @Override
//    public Vehicle updateVehicle(Long carId, VehicleDTO vehicleDTO, MultipartFile[] images ) {
//        Vehicle existingVehicle = vehicleRepository.findById(carId)
//                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + carId));
//
//        Optional<Vehicle> vehicleWithSameNumberPlate = vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate());
//        if (vehicleWithSameNumberPlate.isPresent() && !vehicleWithSameNumberPlate.get().getCarId().equals(carId)) {
//            throw new VehicleAlreadyExistsException("Another vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists.");
//        }
//
//        // Update vehicle fields
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
//        if (featureDTO != null) {
//            CarFeature updatedFeature = modelMapper.map(featureDTO, CarFeature.class);
//            updatedFeature.setId(existingVehicle.getCarFeature().getId()); 
//            updatedFeature.setVehicle(existingVehicle);
//            existingVehicle.setCarFeature(updatedFeature); 
//        }
//
//        // Optional: Remove old images if needed
//        // imageRepository.deleteByVehicle(existingVehicle);
//
//        // Save new images
//        if (images != null) {
//            for (MultipartFile file : images) {
//                try {
//                    Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//                    String imageUrl = uploadResult.get("secure_url").toString();
//
//                    Image image = new Image();
//                    image.setUrl(imageUrl);
//                    image.setVehicle(existingVehicle);
//                    imageRepository.save(image);
//                } catch (IOException e) {
//                    throw new RuntimeException("Image upload failed: " + file.getOriginalFilename(), e);
//                }
//            }
//        }
//
//        return existingVehicle;
//    }
    @Override
    public Vehicle updateVehicle(Long carId, VehicleDTO vehicleDTO, MultipartFile[] images, CarFeatureDTO featureDTO) {
        Vehicle existingVehicle = vehicleRepository.findById(carId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + carId));

        // Check for duplicate number plate
        Optional<Vehicle> vehicleWithSameNumberPlate = vehicleRepository.findByNumberPlate(vehicleDTO.getNumberPlate());
        if (vehicleWithSameNumberPlate.isPresent() && !vehicleWithSameNumberPlate.get().getCarId().equals(carId)) {
            throw new VehicleAlreadyExistsException("Another vehicle with number plate " + vehicleDTO.getNumberPlate() + " already exists.");
        }

        // Update basic vehicle fields manually
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

        // Update or add CarFeature manually
        System.out.println(featureDTO);
        if (featureDTO != null) {
            CarFeature feature = existingVehicle.getCarFeature();
            if (feature == null) {
                feature = new CarFeature();
                feature.setVehicle(existingVehicle);
                existingVehicle.setCarFeature(feature);
            }

            // Manually map each field from DTO to entity
            feature.setSpareTyre(featureDTO.isSpareTyre());
            feature.setToolkit(featureDTO.isToolkit());
            feature.setReverseCamera(featureDTO.isReverseCamera());
            feature.setAdas(featureDTO.isAdas());
            feature.setAbs(featureDTO.isAbs());
            feature.setTractionControl(featureDTO.isTractionControl());
            feature.setFrontAirbags(featureDTO.isFrontAirbags());
            feature.setSideAirbags(featureDTO.isSideAirbags());
            feature.setRearAirbags(featureDTO.isRearAirbags());
            feature.setPowerWindows(featureDTO.isPowerWindows());
            feature.setPowerSteering(featureDTO.isPowerSteering());
            feature.setAirConditioning(featureDTO.isAirConditioning());
            feature.setSunroof(featureDTO.isSunroof());
            feature.setFullBootSpace(featureDTO.isFullBootSpace());
            feature.setPushButtonStart(featureDTO.isPushButtonStart());
            feature.setCruiseControl(featureDTO.isCruiseControl());
            feature.setPanoramicSunroof(featureDTO.isPanoramicSunroof());
            feature.setVoiceControl(featureDTO.isVoiceControl());
            feature.setAirPurifier(featureDTO.isAirPurifier());
            feature.setMusicSystem(featureDTO.isMusicSystem());
            feature.setAirFreshener(featureDTO.isAirFreshener());
            feature.setAuxInput(featureDTO.isAuxInput());
            feature.setAuxCable(featureDTO.isAuxCable());
            feature.setBluetooth(featureDTO.isBluetooth());
            feature.setUsbCharger(featureDTO.isUsbCharger());
            feature.setVentilatedFrontSeats(featureDTO.isVentilatedFrontSeats());
            feature.setSixAirbags(featureDTO.isSixAirbags());
        }

        if (images != null && images.length > 0) {
            // Clear the existing list in-place (important due to orphanRemoval = true)
            if (existingVehicle.getImages() != null) {
                existingVehicle.getImages().clear();
            } else {
                existingVehicle.setImages(new ArrayList<>());
            }

            for (MultipartFile file : images) {
                try {
                    Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("secure_url").toString();
                    Image image = new Image();
                    image.setUrl(imageUrl);
                    image.setVehicle(existingVehicle);
                    existingVehicle.getImages().add(image); 
                } catch (IOException e) {
                    throw new RuntimeException("Image upload failed: " + file.getOriginalFilename(), e);
                }
            }
        }

        return vehicleRepository.save(existingVehicle);
    }


    @Override
    public void deleteVehicle(Long carId) {
        if (!vehicleRepository.existsById(carId)) {
            throw new VehicleNotFoundException("Vehicle not found with ID: " + carId);
        }
        vehicleRepository.deleteById(carId);
    }
}
