package net.igap.service.interfaces;


import net.igap.model.dtos.CreateAccountDto;
import net.igap.model.entity.Address;


public interface AddressService {
  Address saveAddress(CreateAccountDto accountDto);
}
