package net.igap.model.dtos;

import lombok.Data;

@Data
public class GetAccountDto {


  private Long id;
  private String firstName;
  private String lastName;
  private String gender;
  private String dateOfBirth;
  private Long nationalCode;
  private String addressInfo;
  private String provinceName;
  private String cityName;
  private String userName;


}
