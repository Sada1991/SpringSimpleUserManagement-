package net.igap.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {

  @NotBlank(message = "The username is required")
  @Size(min = 3, message = "Should be at least 3 character.")
  private String username;

  @NotBlank(message = "The password is required")
  private String password;
}
