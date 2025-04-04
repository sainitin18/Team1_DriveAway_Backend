package com.DriveAway.project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DriveAway.project.dto.AddressDTO;
import com.DriveAway.project.model.Address;
import com.DriveAway.project.model.User;
import com.DriveAway.project.repository.AddressRepository;
import com.DriveAway.project.repository.UserRepository;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    // Convert Entity to DTO
    private AddressDTO convertToDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setAddressId(address.getAddressId()); // ✅ Ensure ID is set
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setUserId(address.getUser().getUserId());
        return dto;
    }

    // Convert DTO to Entity
    private Address convertToEntity(AddressDTO dto, User user) {
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setUser(user);
        return address;
    }

    // Add a new address
    public AddressDTO addAddress(AddressDTO addressDTO) {
        Optional<User> userOptional = userRepository.findById(addressDTO.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + addressDTO.getUserId());
        }

        Address address = convertToEntity(addressDTO, userOptional.get());
        Address savedAddress = addressRepository.save(address);

        System.out.println("Saved Address ID: " + savedAddress.getAddressId()); // 🔍 DEBUG

        return convertToDTO(savedAddress);
    }

    // Get all addresses for a user
    public List<AddressDTO> getAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findByUserUserId(userId);
        return addresses.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Delete an address by ID
    public void deleteAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new RuntimeException("Address not found with id: " + addressId);
        }
        addressRepository.deleteById(addressId);
    }

    // Update an existing address
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Optional<Address> existingAddressOptional = addressRepository.findById(addressId);
        if (existingAddressOptional.isEmpty()) {
            throw new RuntimeException("Address not found with ID: " + addressId);
        }

        Address existingAddress = existingAddressOptional.get();
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setState(addressDTO.getState());
        existingAddress.setPostalCode(addressDTO.getPostalCode());
        existingAddress.setCountry(addressDTO.getCountry());

        Address updatedAddress = addressRepository.save(existingAddress);
        return convertToDTO(updatedAddress);
    }
}
