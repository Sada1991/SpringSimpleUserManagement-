package net.igap.service;

import java.util.List;
import net.igap.exception.APIException;
import net.igap.exception.ResourceNotFoundException;
import net.igap.exception.WrongCityException;
import net.igap.model.dtos.CityDto;
import net.igap.model.entity.City;
import net.igap.model.entity.Province;
import net.igap.repository.CityRepository;
import net.igap.service.interfaces.CityService;
import net.igap.service.interfaces.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

  private final CityRepository cityRepository;
  private final ProvinceService provinceService;

  @Autowired
  public CityServiceImpl(CityRepository cityRepository, ProvinceService provinceService) {
    this.cityRepository = cityRepository;
    this.provinceService = provinceService;
  }


  @Override
  public CityDto createCity(Long provinceId, CityDto cityDto) {
    Province province = provinceService.findProvinceById(provinceId);
    City city = mapToEntity(cityDto);
    City newCity = getCityIfNameDoesNotDuplicateInProvince(cityDto, province, city);
    return mapToDto(newCity);
  }


  @Override
  public List<CityDto> getCitiesByProvinceId(Long provinceId) {
    Province province = provinceService.findProvinceById(provinceId);

    return province.getCities().stream()
        .filter(City::isEnable)
        .map(this::mapToDto)
        .toList();
  }

  @Override
  public CityDto getCityById(Long provinceId, Long cityId) {
    Province province = provinceService.findProvinceById(provinceId);
    City city = checkCityBelongsToProvinceById(province, cityId);

    return mapToDto(city);
  }
  
  @Override
  public City findCityById(Long cityId) {
    return cityRepository.findById(cityId)
        .filter(City::isEnable)
        .orElseThrow(()->new ResourceNotFoundException(
            String.format("City not found with id : '%s'", cityId))
        );
  }

  @Override
  public CityDto updateCity(Long provinceId, Long cityId, CityDto cityDto) {
    Province province = provinceService.findProvinceById(provinceId);
    City city = checkCityBelongsToProvinceById(province, cityId);

    City updatedCity = getCityIfNameDoesNotDuplicateInProvince(cityDto, province, city);
    return mapToDto(updatedCity);
  }

  @Override
  public void deleteCity(Long provinceId, Long cityId) {
    Province province = provinceService.findProvinceById(provinceId);
    City city = checkCityBelongsToProvinceById(province, cityId);

    city.setEnable(false);
    cityRepository.save(city);
  }

  private City getCityIfNameDoesNotDuplicateInProvince(CityDto cityDto, Province province, City city) {
    boolean cityNameIsExist = province.getCities().stream()
        .anyMatch(c -> c.getCityName().equals(cityDto.getCityName()) && c.isEnable());

    if(!cityNameIsExist) {
      city.setProvince(province);
      city.setCityName(cityDto.getCityName());
      return cityRepository.save(city);
    }else
      throw new WrongCityException(
          String.format("City [%s] is already exists in Province [%s].",
              cityDto.getCityName(), province.getProvinceName())
      );
  }

  private City mapToEntity(CityDto cityDto) {
    City city = new City();
    city.setCityName(cityDto.getCityName());
    return city;
  }

  private CityDto mapToDto(City city) {
    CityDto cityDto = new CityDto();

    cityDto.setId(city.getId());
    cityDto.setCityName(city.getCityName());

    return cityDto;
  }

  private City checkCityBelongsToProvinceById(Province province, Long cityId) {
    City city = findCityById(cityId);

    if(!city.getProvince().getId().equals(province.getId())){
      throw new APIException("City does not belong to Province.");
    }else
        return city;
  }


}
