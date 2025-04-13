package com.DriveAway.project.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AddressServiceImplTest {

    @Mock private AddressRepository addressRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private AddressServiceImpl addressService;

    private User user;
    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(101L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        address = new Address();
        address.setAddressId(1L);
        address.setStreet("123 Main Street");
        address.setCity("Hyderabad");
        address.setState("Telangana");
        address.setPostalCode("500001");
        address.setCountry("India");
        address.setUser(user);

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
    @DisplayName("Add Address - Success")
    void givenAddressDTO_whenAddAddress_thenReturnSavedAddress() {
        when(userRepository.findById(101L)).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO result = addressService.addAddress(addressDTO);

        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("123 Main Street");
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("Get Addresses By User ID - Success")
    void givenUserId_whenGetUserAddresses_thenReturnList() {
        when(userRepository.findById(101L)).thenReturn(Optional.of(user));
        when(addressRepository.findByUserUserId(101L)).thenReturn(List.of(address));

        List<AddressDTO> result = addressService.getAddressesByUserId(101L);

        assertThat(result).isNotEmpty();
        verify(addressRepository).findByUserUserId(101L);
    }

    @Test
    @DisplayName("Update Address - Success")
    void givenAddressDTO_whenUpdateAddress_thenReturnUpdated() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO result = addressService.updateAddress(1L, addressDTO);

        assertThat(result.getAddressId()).isEqualTo(1L);
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    @DisplayName("Delete Address - Success")
    void givenAddressId_whenDeleteAddress_thenNoException() {
        when(addressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(1L);

        addressService.deleteAddress(1L);
        verify(addressRepository).deleteById(1L);
    }
}
