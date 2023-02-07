package net.igap.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CityDto {

  private Long id;

  @NotBlank(message = "The city name is required")
  @Size(min=3, message = "Should be at least 3 character.")
  private String cityName;


}
