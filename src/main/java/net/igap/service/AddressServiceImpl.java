package net.igap.service;

import net.igap.model.dtos.CreateAccountDto;
import net.igap.model.dtos.UpdateDto;
import net.igap.model.entity.Address;
import net.igap.model.entity.City;
import net.igap.model.entity.Province;
import net.igap.repository.AddressRepository;
import net.igap.service.interfaces.AddressService;
import net.igap.service.interfaces.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AddressServiceImpl implements AddressService {
  private final AddressRepository addressRepository;
  private final ProvinceService provinceService;

  @Autowired
  public AddressServiceImpl(AddressRepository addressRepository, ProvinceService provinceService) {
    this.addressRepository = addressRepository;
    this.provinceService = provinceService;
  }

  @Override
  public Address saveAddress(CreateAccountDto accountDto){

    Province foundedProvince = provinceService.checkProvinceExistsByName(accountDto.getProvinceName());

    UpdateDto updateDto = new UpdateDto();
    updateDto.setAddressInfo(accountDto.getAddressInfo());
    updateDto.setProvinceName(accountDto.getProvinceName());
    updateDto.setCityName(accountDto.getCityName());
    City city = provinceService.checkCityBelongsToProvince(updateDto, foundedProvince);

    Address newAddress = new Address();
    newAddress.setAddressInfo(accountDto.getAddressInfo());
    newAddress.setCity(city);
    newAddress.setProvince(foundedProvince);

    return addressRepository.save(newAddress);
  }
}
