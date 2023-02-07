package net.igap.controller;

import static net.igap.utils.AppConstants.DEFAULT_PAGE_NUMBER;
import static net.igap.utils.AppConstants.DEFAULT_PAGE_SIZE;

import jakarta.validation.Valid;
import net.igap.model.dtos.ProvinceDto;
import net.igap.model.dtos.ResponseDtoWithPagination;
import net.igap.service.interfaces.ProvinceService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/provinces")
public class ProvinceController {

  private final ProvinceService provinceService;

  @Autowired
  public ProvinceController(ProvinceService provinceService) {
    this.provinceService = provinceService;
  }

  @PostMapping(value={"", "/"})
  public ResponseEntity<ProvinceDto> createProvince(@Valid @RequestBody ProvinceDto provinceDto){
    return new ResponseEntity<>(provinceService.createProvince(provinceDto), HttpStatus.CREATED);
  }

  @GetMapping(value={"", "/"})
  public ResponseDtoWithPagination<ProvinceDto> getAllProvinces(
      @RequestParam(value="pageNo",defaultValue = DEFAULT_PAGE_NUMBER,required = false) int pageNo,
      @RequestParam(value="pageSize",defaultValue = DEFAULT_PAGE_SIZE,required = false) int pageSize){
    return provinceService.getAllProvinces(pageNo, pageSize);
  }

  @GetMapping("/{provinceId}")
  public ResponseEntity<ProvinceDto> getProvinceById(@PathVariable Long provinceId){
    return ResponseEntity.ok(provinceService.getProvinceById(provinceId));
  }

  @PutMapping("/{provinceId}")
  public ResponseEntity<ProvinceDto> updateProvince(@Valid @RequestBody ProvinceDto provinceDto,
                                                    @PathVariable Long provinceId){
    return ResponseEntity.ok(provinceService.updateProvince(provinceDto,provinceId));
  }

  @DeleteMapping("/{provinceId}")
  public ResponseEntity<String> deleteProvince(@PathVariable Long provinceId){
    provinceService.deleteProvinceById(provinceId);
    return ResponseEntity.ok("Province and related cities deleted successfully!");
  }
}
