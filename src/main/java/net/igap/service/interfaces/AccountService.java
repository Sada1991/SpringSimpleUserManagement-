package net.igap.service.interfaces;

import net.igap.model.dtos.CreateAccountResponseDto;
import net.igap.model.dtos.CreateAccountDto;
import net.igap.model.dtos.GetAccountDto;
import net.igap.model.dtos.ResponseDtoWithPagination;
import net.igap.model.dtos.UpdateDto;
import net.igap.model.entity.Account;

public interface AccountService {

  CreateAccountResponseDto createAccount(CreateAccountDto accountDto);

  ResponseDtoWithPagination<GetAccountDto> getAllAccounts(int pageNo, int pageSize);

  GetAccountDto getAccountByUsername(String username);

  Account findAccountByUsername(String username);

  Account findAccountById(Long accountId);

  void checkNationalCodeExists(Long nationalCode);

  GetAccountDto updateAccount(UpdateDto accountDto, String username);

  void deleteAccountById(String username);

}
