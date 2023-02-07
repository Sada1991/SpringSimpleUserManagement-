package net.igap.model.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class ProvinceDto {

  private Long id;

  @NotBlank(message = "The province name is required")
  @Size(min=3, message = "Should be at least 3 character.")
  private String provinceName;

  private Set<CityDto> cities;
}
