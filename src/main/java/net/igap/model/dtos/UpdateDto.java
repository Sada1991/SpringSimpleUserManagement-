package net.igap.model.dtos;

import lombok.Data;

@Data
public class UpdateDto {

  private Long id;
  private String addressInfo;
  private String provinceName;
  private String cityName;
  private String password;

}
