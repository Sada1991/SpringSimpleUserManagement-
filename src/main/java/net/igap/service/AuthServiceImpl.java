package net.igap.service;

import net.igap.model.dtos.LoginDto;
import net.igap.repository.AccountRepository;
import net.igap.repository.RoleRepository;
import net.igap.security.JsonWebTokenProvider;
import net.igap.service.interfaces.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private AuthenticationManager authenticationManager;
  private AccountRepository accountRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;
  private JsonWebTokenProvider jwtTokenProvider;


  public AuthServiceImpl(AuthenticationManager authenticationManager,
      AccountRepository accountRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      JsonWebTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.accountRepository = accountRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public String login(LoginDto loginDto) {

    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        loginDto.getUsername(), loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtTokenProvider.generateToken(authentication);

    return token;
  }



}
