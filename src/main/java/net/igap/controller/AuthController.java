package net.igap.controller;

import jakarta.validation.Valid;
import java.util.List;
import net.igap.model.dtos.CreateAccountDto;
import net.igap.model.dtos.CreateAccountResponseDto;
import net.igap.model.dtos.JwtAuthResponse;
import net.igap.model.dtos.LoginDto;
import net.igap.model.entity.Person;
import net.igap.repository.PersonRepository;
import net.igap.service.interfaces.AccountService;
import net.igap.service.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final AuthService authService;
  private final AccountService accountService;
  private final PersonRepository personRepository;

  public AuthController(AuthService authService, AccountService accountService,
      PersonRepository personRepository) {
    this.authService = authService;
    this.accountService = accountService;
    this.personRepository = personRepository;
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){

    String token = authService.login(loginDto);

    JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
    jwtAuthResponse.setAccessToken(token);

    return ResponseEntity.ok(jwtAuthResponse);
  }

  @PostMapping("/register")
  public ResponseEntity<CreateAccountResponseDto> createAccount(@Valid @RequestBody CreateAccountDto accountDto) {
    return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
  }

  @GetMapping("/test")
//  @Cacheable("test")
  public List<Person> getPersons(){
    return personRepository.findAll();
  }

}
