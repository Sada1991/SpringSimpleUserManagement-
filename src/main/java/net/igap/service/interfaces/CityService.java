package net.igap.service.interfaces;

import java.util.List;
import net.igap.model.dtos.CityDto;
import net.igap.model.entity.City;

public interface CityService {

  CityDto createCity(Long provinceId, CityDto cityDto);

  List<CityDto> getCitiesByProvinceId(Long provinceId);

  CityDto getCityById(Long provinceId, Long cityId);

  City findCityById(Long cityId);

  CityDto updateCity(Long provinceId, Long cityId, CityDto cityDto);

  void deleteCity(Long provinceId, Long cityId);
}
