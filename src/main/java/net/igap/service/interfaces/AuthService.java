package net.igap.service.interfaces;

import net.igap.model.dtos.LoginDto;

public interface AuthService {
  String login(LoginDto loginDto);
}
