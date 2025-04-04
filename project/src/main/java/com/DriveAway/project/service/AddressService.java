package com.DriveAway.project.service;

import java.util.List;
import com.DriveAway.project.dto.AddressDTO;

public interface AddressService {
    public AddressDTO addAddress(AddressDTO addressDTO);
    public List<AddressDTO> getAddressesByUserId(Long userId);
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);
    public void deleteAddress(Long addressId);
}
