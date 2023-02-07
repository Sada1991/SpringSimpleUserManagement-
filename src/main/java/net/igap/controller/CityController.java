package net.igap.controller;

import jakarta.validation.Valid;
import java.util.List;
import net.igap.model.dtos.CityDto;
import net.igap.service.interfaces.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provinces")
public class CityController {

  private final CityService cityService;

  @Autowired
  public CityController(CityService cityService) {
    this.cityService = cityService;
  }

  @PostMapping (value = {"/{provinceId}/cities","/{provinceId}/cities/"})
  public ResponseEntity<CityDto> createCity(@PathVariable Long provinceId,
                                            @Valid @RequestBody CityDto cityDto){
    return new ResponseEntity<>(cityService.createCity(provinceId,cityDto), HttpStatus.CREATED);
  }

  @GetMapping(value = {"/{provinceId}/cities","/{provinceId}/cities/"})
  public List<CityDto> getAllCities(@PathVariable Long provinceId){
    return cityService.getCitiesByProvinceId(provinceId);
  }

  @GetMapping("/{provinceId}/cities/{cityId}")
  public ResponseEntity<CityDto> getCityById(@PathVariable Long provinceId,
                                             @PathVariable Long cityId){
    return ResponseEntity.ok(cityService.getCityById(provinceId,cityId));
  }

  @PutMapping("/{provinceId}/cities/{cityId}")
  public ResponseEntity<CityDto> updateCity(@PathVariable Long provinceId,
                                            @PathVariable Long cityId,
                                            @Valid @RequestBody CityDto cityDto){
    return ResponseEntity.ok(cityService.updateCity(provinceId, cityId, cityDto));
  }

  @DeleteMapping("/{provinceId}/cities/{cityId}")
  public ResponseEntity<String> updateCity(@PathVariable Long provinceId,
                                           @PathVariable Long cityId){
    cityService.deleteCity(provinceId,cityId);
    return new ResponseEntity<>("City is deleted successfully!",HttpStatus.OK);
  }
}
