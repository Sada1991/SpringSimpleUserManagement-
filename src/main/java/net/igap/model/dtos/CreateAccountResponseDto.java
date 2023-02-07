package net.igap.model.dtos;

import lombok.Data;

@Data
public class CreateAccountResponseDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String userName;

}
