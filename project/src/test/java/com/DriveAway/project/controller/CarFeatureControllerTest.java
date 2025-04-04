package com.DriveAway.project.controller;

import com.DriveAway.project.dto.CarFeatureDTO;
import com.DriveAway.project.service.CarFeatureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CarFeatureControllerTest {

    @Mock
    private CarFeatureServiceImpl carFeatureService;

    @InjectMocks
    private CarFeatureController carFeatureController;

    private CarFeatureDTO carFeatureDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        carFeatureDTO = new CarFeatureDTO();
        carFeatureDTO.setCarId(1L);
        carFeatureDTO.setBluetooth(true);
        carFeatureDTO.setReverseCamera(true);
        carFeatureDTO.setSixAirbags(true);
        carFeatureDTO.setCruiseControl(true);
        carFeatureDTO.setPowerSteering(true);
        carFeatureDTO.setSunroof(true);
        // Set other features as needed...
    }

    @Test
    @DisplayName("JUnit test for adding car features (Success)")
    void givenCarFeatureDTO_whenAddCarFeatures_thenReturnSavedDTO() {
        when(carFeatureService.addCarFeatures(carFeatureDTO)).thenReturn(carFeatureDTO);

        ResponseEntity<CarFeatureDTO> response = carFeatureController.addCarFeatures(carFeatureDTO);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCarId()).isEqualTo(1L);
        verify(carFeatureService, times(1)).addCarFeatures(carFeatureDTO);
    }

    @Test
    @DisplayName("JUnit test for updating car features (Success)")
    void givenCarFeatureDTO_whenUpdateCarFeatures_thenReturnUpdatedDTO() {
        when(carFeatureService.updateCarFeatures(1L, carFeatureDTO)).thenReturn(carFeatureDTO);

        ResponseEntity<CarFeatureDTO> response = carFeatureController.updateCarFeatures(1L, carFeatureDTO);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isBluetooth()).isTrue();
        verify(carFeatureService, times(1)).updateCarFeatures(1L, carFeatureDTO);
    }

    @Test
    @DisplayName("JUnit test for getting car features (Success)")
    void givenCarId_whenGetCarFeatures_thenReturnCarFeatureDTO() {
        when(carFeatureService.getCarFeatures(1L)).thenReturn(carFeatureDTO);

        ResponseEntity<CarFeatureDTO> response = carFeatureController.getCarFeatures(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCarId()).isEqualTo(1L);
        verify(carFeatureService, times(1)).getCarFeatures(1L);
    }
}
