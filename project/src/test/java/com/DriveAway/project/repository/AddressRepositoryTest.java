package com.DriveAway.project.repository;

import com.DriveAway.project.model.Address;
import com.DriveAway.project.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Address address;

    @BeforeEach
    public void setUp() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 6);

        user = new User();
        user.setUsername("John Doe");
        user.setEmail("john" + uniqueId + "@example.com");
        user.setPassword("password123");
        user.setAadharNumber("123456" + uniqueId);
        user.setDrivingLicense("DL" + uniqueId);
        user.setMobileNumber("98" + uniqueId);  // Ensures uniqueness
        user.setAltMobileNumber("91" + uniqueId);
        user.setRole("USER");
        user.setStatus("ACTIVE");
        user = userRepository.save(user);

        address = new Address();
        address.setStreet("123 Main Street");
        address.setCity("Hyderabad");
        address.setState("Telangana");
        address.setPostalCode("500081");
        address.setCountry("India");
        address.setUser(user);
        address = addressRepository.save(address);
    }

    @Test
    @DisplayName("Test saving an address")
    @Order(1)
    public void givenAddress_whenSave_thenReturnSavedAddress() {
        assertThat(address).isNotNull();
        assertThat(address.getAddressId()).isNotNull();
        assertThat(address.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Test finding address by user ID")
    @Order(2)
    public void givenUserId_whenFindByUserId_thenReturnAddressList() {
        List<Address> addresses = addressRepository.findByUserUserId(user.getUserId());
        assertThat(addresses).isNotEmpty();
        assertThat(addresses.get(0).getUser().getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    @DisplayName("Test deleting an address by ID")
    @Order(3)
    public void givenAddressId_whenDelete_thenRemoveAddress() {
        addressRepository.deleteById(address.getAddressId());
        boolean exists = addressRepository.existsById(address.getAddressId());
        assertThat(exists).isFalse();
    }

    @AfterEach
    public void cleanUp() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }
}
