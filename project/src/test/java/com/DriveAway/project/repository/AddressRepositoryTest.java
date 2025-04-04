package com.DriveAway.project.repository;

import com.DriveAway.project.model.Address;
import com.DriveAway.project.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Uses actual DB, not H2
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Address address;

    @BeforeEach
    void setUp() {
        // ✅ Given: Create and Save User (Ensuring All Required Fields Are Set)
        user = new User();
        user.setUsername("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setAadharNumber("123456789012"); // ✅ Ensuring Aadhar Number is Set
        user.setDrivingLicense("DL1234567890"); // ✅ Ensuring Driving License is Set
        user.setMobileNumber("9876543210");
        user.setAltMobileNumber("9123456789");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        user = userRepository.save(user); // ✅ Persist the User

        // ✅ Given: Create and Save Address linked to User
        address = new Address();
        address.setStreet("123 Main Street");
        address.setCity("Hyderabad");
        address.setState("Telangana");
        address.setPostalCode("500081");
        address.setCountry("India");
        address.setUser(user);

        address = addressRepository.save(address); // ✅ Persist the Address
    }

    @Test
    void givenAddress_whenSave_thenReturnSavedAddress() {
        // ✅ Success Case: Address is successfully saved
        assertThat(address).isNotNull();
        assertThat(address.getAddressId()).isNotNull();
        assertThat(address.getUser()).isNotNull(); // ✅ Ensure User is Assigned
        assertThat(address.getUser().getDrivingLicense()).isNotNull(); // ✅ Driving License should not be null
    }

    @Test
    void givenUserId_whenFindByUserId_thenReturnAddressList() {
        // ✅ When retrieving addresses by user ID
        List<Address> addresses = addressRepository.findByUserUserId(user.getUserId());

        // ✅ Success Case: The list is not empty and contains the correct user's addresses
        assertThat(addresses).isNotEmpty();
        assertThat(addresses.get(0).getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(addresses.get(0).getUser().getDrivingLicense()).isEqualTo(user.getDrivingLicense()); // ✅ Ensuring Correct License
    }

    @Test
    void givenAddressId_whenDelete_thenRemoveAddress() {
        // ✅ When deleting an address by ID
        addressRepository.deleteById(address.getAddressId());

        // ✅ Success Case: The address no longer exists in the database
        assertThat(addressRepository.existsById(address.getAddressId())).isFalse();
    }
}
