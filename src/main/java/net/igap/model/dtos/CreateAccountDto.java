package net.igap.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountDto {


  private Long id;

  @NotBlank(message = "The first name is required")
  @Size(min=3, message = "Should be at least 3 character.")
  private String firstName;

  @NotBlank(message = "The last name is required")
  @Size(min=3 , message = "Should be at least 3 character.")
  private String lastName;

  @NotBlank(message = "The gender is required")
  private String gender;

  @NotBlank(message = "The date of birth is required")
  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date input - (yyyy.mm.dd)")
  private String dateOfBirth;

  @NotBlank(message = "The national code is required")
  private String nationalCode;

  @NotBlank(message = "The address Info is required")
  private String addressInfo;

  @NotBlank(message = "The province name is required")
  private String provinceName;

  @NotBlank(message = "The city name is required")
  private String cityName;

  @NotBlank(message = "The user name is required")
  @Size(min = 3, message = "Should be at least 3 character.")
  private String userName;

  @NotBlank(message = "The password is required")
  @Size(min = 6, message = "Should be at least 6 character.")
  private String password;

}
