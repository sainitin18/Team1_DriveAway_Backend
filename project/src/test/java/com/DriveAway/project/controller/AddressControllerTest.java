package com.DriveAway.project.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Arrays;

import com.DriveAway.project.dto.AddressDTO;
import com.DriveAway.project.service.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @InjectMocks private AddressController addressController;
    @Mock private AddressServiceImpl addressService;

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

    @Test
    void testAddAddress() {
        when(addressService.addAddress(any(AddressDTO.class))).thenReturn(addressDTO);

        ResponseEntity<AddressDTO> response = addressController.addAddress(addressDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("123 Main Street", response.getBody().getStreet());
    }

    @Test
    void testGetUserAddresses() {
        when(addressService.getAddressesByUserId(101L)).thenReturn(Arrays.asList(addressDTO));

        ResponseEntity<List<AddressDTO>> response = addressController.getUserAddresses(101L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testUpdateAddress() {
        AddressDTO updated = new AddressDTO();
        updated.setAddressId(1L);
        updated.setStreet("456 New Street");
        updated.setCity("Bangalore");
        updated.setState("Karnataka");
        updated.setPostalCode("560001");
        updated.setCountry("India");
        updated.setUserId(101L);

        when(addressService.updateAddress(eq(1L), any(AddressDTO.class))).thenReturn(updated);

        ResponseEntity<AddressDTO> response = addressController.updateAddress(1L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("456 New Street", response.getBody().getStreet());
    }

    @Test
    void testDeleteAddress() {
        doNothing().when(addressService).deleteAddress(1L);

        ResponseEntity<String> response = addressController.deleteAddress(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Address deleted successfully!", response.getBody());
    }
}
