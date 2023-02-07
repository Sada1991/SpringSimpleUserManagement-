package net.igap.service.interfaces;

import net.igap.model.dtos.ProvinceDto;
import net.igap.model.dtos.ResponseDtoWithPagination;
import net.igap.model.dtos.UpdateDto;
import net.igap.model.entity.City;
import net.igap.model.entity.Province;

public interface ProvinceService {

  Province checkProvinceExistsByName(String provinceName);
  City checkCityBelongsToProvince(UpdateDto accountDto, Province province);
  ResponseDtoWithPagination<ProvinceDto> getAllProvinces(int pageNo, int pageSize);
  ProvinceDto createProvince(ProvinceDto provinceDto);
  ProvinceDto getProvinceById(Long provinceId);
  Province findProvinceById(Long provinceId);
  ProvinceDto updateProvince(ProvinceDto provinceDto, Long provinceId);
  void deleteProvinceById(Long provinceId);

}
