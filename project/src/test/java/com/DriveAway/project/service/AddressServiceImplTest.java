package com.DriveAway.project.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.DriveAway.project.dto.AddressDTO;
import com.DriveAway.project.model.Address;
import com.DriveAway.project.model.User;
import com.DriveAway.project.repository.AddressRepository;
import com.DriveAway.project.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Address address;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("Alice");
        user.setEmail("alice@example.com");

        address = new Address();
        address.setAddressId(1L);
        address.setStreet("123 Main St");
        address.setCity("New York");
        address.setState("NY");
        address.setPostalCode("10001");
        address.setCountry("USA");
        address.setUser(user);
    }

    @Test
    void testAddAddress_Success() {
        AddressDTO dto = new AddressDTO();
        dto.setUserId(1L);
        dto.setStreet("123 Main St");
        dto.setCity("New York");
        dto.setState("NY");
        dto.setPostalCode("10001");
        dto.setCountry("USA");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO result = addressService.addAddress(dto);

        assertNotNull(result);
        assertEquals("New York", result.getCity());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void testAddAddress_UserNotFound() {
        AddressDTO dto = new AddressDTO();
        dto.setUserId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            addressService.addAddress(dto);
        });

        assertEquals("User not found with ID: 2", ex.getMessage());
    }

    @Test
    void testGetAddressesByUserId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressRepository.findByUserUserId(1L)).thenReturn(List.of(address));

        List<AddressDTO> result = addressService.getAddressesByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("123 Main St", result.get(0).getStreet());
    }

    @Test
    void testGetAddressesByUserId_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            addressService.getAddressesByUserId(1L);
        });

        assertEquals("User not found with ID: 1", ex.getMessage());
    }

    @Test
    void testDeleteAddress_Success() {
        when(addressRepository.existsById(1L)).thenReturn(true);

        addressService.deleteAddress(1L);

        verify(addressRepository).deleteById(1L);
    }

    @Test
    void testDeleteAddress_NotFound() {
        when(addressRepository.existsById(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            addressService.deleteAddress(1L);
        });

        assertEquals("Address not found with id: 1", ex.getMessage());
    }

    @Test
    void testUpdateAddress_Success() {
        AddressDTO dto = new AddressDTO();
        dto.setStreet("456 Elm St");
        dto.setCity("Chicago");
        dto.setState("IL");
        dto.setPostalCode("60601");
        dto.setCountry("USA");

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO updated = addressService.updateAddress(1L, dto);

        assertEquals("Chicago", updated.getCity());
        verify(addressRepository).save(address);
    }

    @Test
    void testUpdateAddress_NotFound() {
        AddressDTO dto = new AddressDTO();

        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            addressService.updateAddress(1L, dto);
        });

        assertEquals("Address not found with ID: 1", ex.getMessage());
    }
}
