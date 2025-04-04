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

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

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

        // ✅ Corrected way to initialize AddressDTO
        addressDTO = new AddressDTO();
        addressDTO.setAddressId(address.getAddressId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setPostalCode(address.getPostalCode());
        addressDTO.setCountry(address.getCountry());
        addressDTO.setUserId(address.getUser().getUserId());
    }

    @Test
    @DisplayName("JUnit test for adding an address (Success)")
    void givenAddressDTO_whenAddAddress_thenReturnSavedAddress() {
        when(userRepository.findById(101L)).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO savedAddress = addressService.addAddress(addressDTO);

        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getStreet()).isEqualTo("123 Main Street");
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("JUnit test for getting all addresses of a user (Success)")
    void givenUserId_whenGetUserAddresses_thenReturnAddressList() {
        when(addressRepository.findByUserUserId(101L)).thenReturn(Arrays.asList(address));

        List<AddressDTO> addresses = addressService.getAddressesByUserId(101L);

        assertThat(addresses).isNotEmpty();
        assertThat(addresses.size()).isEqualTo(1);
        verify(addressRepository, times(1)).findByUserUserId(101L);
    }

    @Test
    @DisplayName("JUnit test for updating an address (Success)")
    void givenAddressDTO_whenUpdateAddress_thenReturnUpdatedAddress() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDTO updatedAddress = addressService.updateAddress(1L, addressDTO);

        assertThat(updatedAddress).isNotNull();
        assertThat(updatedAddress.getStreet()).isEqualTo("123 Main Street");
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    @DisplayName("JUnit test for deleting an address (Success)")
    void givenAddressId_whenDeleteAddress_thenRemoveAddress() {
        when(addressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(1L);

        addressService.deleteAddress(1L);

        verify(addressRepository, times(1)).deleteById(1L);
    }
}
