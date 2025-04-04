package com.DriveAway.project.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import com.DriveAway.project.dto.AddressDTO;
import com.DriveAway.project.service.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @InjectMocks
    private AddressController addressController;

    @Mock
    private AddressServiceImpl addressService;

    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        addressDTO = new AddressDTO();
        addressDTO.setAddressId(1L);
        addressDTO.setStreet("123 Main Street");
        addressDTO.setCity("Hyderabad");
        addressDTO.setState("Telangana");
        addressDTO.setPostalCode("500001");
        addressDTO.setCountry("India");
        addressDTO.setUserId(101L);
    }

    // ✅ Test Adding an Address
    @Test
    void testAddAddress() {
        when(addressService.addAddress(any(AddressDTO.class))).thenReturn(addressDTO);

        ResponseEntity<AddressDTO> response = addressController.addAddress(addressDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("123 Main Street", response.getBody().getStreet());
    }

    // ✅ Test Getting User Addresses
    @Test
    void testGetUserAddresses() {
        Long userId = 101L;
        List<AddressDTO> mockAddresses = Arrays.asList(addressDTO);

        when(addressService.getAddressesByUserId(userId)).thenReturn(mockAddresses);

        ResponseEntity<List<AddressDTO>> response = addressController.getUserAddresses(userId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    // ✅ Test Updating an Address
    @Test
    void testUpdateAddress() {
        Long addressId = 1L;
        AddressDTO updatedAddressDTO = new AddressDTO();
        updatedAddressDTO.setAddressId(addressId);
        updatedAddressDTO.setStreet("456 New Street");
        updatedAddressDTO.setCity("Bangalore");
        updatedAddressDTO.setState("Karnataka");
        updatedAddressDTO.setPostalCode("560001");
        updatedAddressDTO.setCountry("India");
        updatedAddressDTO.setUserId(101L);

        when(addressService.updateAddress(eq(addressId), any(AddressDTO.class))).thenReturn(updatedAddressDTO);

        ResponseEntity<AddressDTO> response = addressController.updateAddress(addressId, updatedAddressDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("456 New Street", response.getBody().getStreet());
    }

    // ✅ Test Deleting an Address
    @Test
    void testDeleteAddress() {
        Long addressId = 1L;

        doNothing().when(addressService).deleteAddress(addressId);

        ResponseEntity<String> response = addressController.deleteAddress(addressId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Address deleted successfully!", response.getBody());
    }
}
