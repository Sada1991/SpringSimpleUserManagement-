package net.igap.service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.igap.exception.ResourceNotFoundException;
import net.igap.exception.WrongCityException;
import net.igap.exception.WrongProvinceException;
import net.igap.model.dtos.CityDto;
import net.igap.model.dtos.ProvinceDto;
import net.igap.model.dtos.ResponseDtoWithPagination;
import net.igap.model.dtos.UpdateDto;
import net.igap.model.entity.City;
import net.igap.model.entity.Province;
import net.igap.repository.ProvinceRepository;
import net.igap.service.interfaces.ProvinceService;
import net.igap.utils.Helpers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProvinceServiceImpl implements ProvinceService {

  private final ProvinceRepository provinceRepository;

  public ProvinceServiceImpl(ProvinceRepository provinceRepository) {
    this.provinceRepository = provinceRepository;
  }


  @Override
  public Province checkProvinceExistsByName(String provinceName) {
    return provinceRepository.findProvinceByEnableIsTrueAndProvinceName(provinceName)
        .orElseThrow(()-> new WrongProvinceException(
            String.format("Province with name [%s] does not exists!",provinceName)));
  }

  @Override
  public City checkCityBelongsToProvince(UpdateDto accountDto, Province province) {
    return province.getCities().stream()
        .filter(city -> city.getCityName().equals(accountDto.getCityName()) && city.isEnable())
        .findAny()
        .orElseThrow(() -> new WrongCityException(
            String.format("City [%s] is not belongs to Province [%s].",
                accountDto.getCityName(), province.getProvinceName())
        ));
  }

  @Override
  public ResponseDtoWithPagination<ProvinceDto> getAllProvinces(int pageNo, int pageSize) {

    Pageable pageable = PageRequest.of(pageNo,pageSize);
    Page<Province> provinces = provinceRepository.findAllByEnableIsTrue(pageable);
    List<ProvinceDto> contents = provinces.getContent().stream()
        .filter(Province::isEnable)
        .map(this::mapToDto).toList();

    return Helpers.mapToDtoResponseWithPagination(provinces, contents);
  }

  @Override
  public ProvinceDto createProvince(ProvinceDto provinceDto) {
    Optional<Province> foundedProvince =
        provinceRepository.findProvinceByEnableIsTrueAndProvinceName(provinceDto.getProvinceName());
    if(foundedProvince.isPresent()) {
      throw new WrongProvinceException
          (String.format("Province with name [%s] is already exists!",
              provinceDto.getProvinceName()));
    }
    Province province = mapToEntity(provinceDto);
    Province newProvince = provinceRepository.save(province);
    return mapToDto(newProvince);
  }

  @Override
  public ProvinceDto getProvinceById(Long provinceId) {
    Province province = findProvinceById(provinceId);
    if(province.isEnable())
        return mapToDto(province);
    else
      throw new ResourceNotFoundException(String.format("Province not found with id : '%s'", provinceId));
  }

  @Override
  public Province findProvinceById(Long provinceId) {
    return provinceRepository.findByEnableIsTrueAndId(provinceId)
        .orElseThrow(()-> new ResourceNotFoundException(
            String.format("Province not found with id : '%s'", provinceId)));
  }

  @Override
  public ProvinceDto updateProvince(ProvinceDto provinceDto, Long provinceId) {
    Province province = findProvinceById(provinceId);

    if(provinceDto.getProvinceName()!= null){
      boolean provinceExists = provinceRepository
              .findProvinceByEnableIsTrueAndProvinceName(provinceDto.getProvinceName())
              .isPresent();
      if(provinceExists)
        throw new WrongProvinceException(
            String.format("Province with name [%s] is already exists!", provinceDto.getProvinceName()));
    }

    province.setProvinceName(provinceDto.getProvinceName());
    Province updatedProvince = provinceRepository.save(province);
    return mapToDto(updatedProvince);
  }

  @Override
  public void deleteProvinceById(Long provinceId) {
    Province province = findProvinceById(provinceId);
    province.setEnable(false);
    province.getCities().forEach(city -> city.setEnable(false));
    provinceRepository.save(province);
  }

  private Province mapToEntity(ProvinceDto provinceDto) {
   Province province = new Province();

   province.setProvinceName(provinceDto.getProvinceName());
   return province;
  }

  private ProvinceDto mapToDto(Province province) {
    ProvinceDto provinceDto = new ProvinceDto();

    Set<City> cities = province.getCities().stream()
        .filter(City::isEnable).collect(Collectors.toSet());
    Set<CityDto> cityDtos = new HashSet<>();

    cities.forEach(city -> {
      CityDto newCityDto = new CityDto();
      String cityName = city.getCityName();
      Long cityId = city.getId();
      newCityDto.setId(cityId);
      newCityDto.setCityName(cityName);
      cityDtos.add(newCityDto);
    });

    provinceDto.setId(province.getId());
    provinceDto.setProvinceName(province.getProvinceName());
    provinceDto.setCities(cityDtos);

    return provinceDto;
  }
}
