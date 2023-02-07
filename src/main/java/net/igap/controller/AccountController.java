package net.igap.controller;

import static net.igap.utils.AppConstants.DEFAULT_PAGE_NUMBER;
import static net.igap.utils.AppConstants.DEFAULT_PAGE_SIZE;
import static net.igap.utils.Helpers.getLoggedInAccount;
import static net.igap.utils.Helpers.isLoggedInAccountAdmin;

import jakarta.validation.Valid;
import net.igap.model.dtos.GetAccountDto;
import net.igap.model.dtos.ResponseDtoWithPagination;
import net.igap.model.dtos.UpdateDto;
import net.igap.service.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

  private final AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  @RequestMapping(value={"", "/"})
//  @Cacheable("accounts")
  public ResponseDtoWithPagination<GetAccountDto> getAllAccounts(
      @RequestParam(value="pageNo",defaultValue = DEFAULT_PAGE_NUMBER,required = false) int pageNo,
      @RequestParam(value="pageSize",defaultValue = DEFAULT_PAGE_SIZE,required = false) int pageSize){

    return accountService.getAllAccounts(pageNo, pageSize);
  }

  @GetMapping("/{username}")
//  @Cacheable(key = "#accountId", value = "account")
  public ResponseEntity<GetAccountDto> getAccountById(@PathVariable String username){
    UserDetails loggedInAccount = getLoggedInAccount();
    if(loggedInAccount.getUsername().equals(username) || isLoggedInAccountAdmin(loggedInAccount))
      return ResponseEntity.ok(accountService.getAccountByUsername(username));
    else
      throw new AccessDeniedException("Access is denied.");
  }

  @PutMapping("/{username}")
//  @CacheEvict(value = {"accounts","account"},allEntries = true)
  public ResponseEntity<GetAccountDto> updateAccount(@Valid @RequestBody UpdateDto accountDto,
                                                        @PathVariable String username){
    UserDetails loggedInAccount = getLoggedInAccount();
    if(loggedInAccount.getUsername().equals(username) || isLoggedInAccountAdmin(loggedInAccount))
      return ResponseEntity.ok(accountService.updateAccount(accountDto,username));
    else
      throw new AccessDeniedException("Access is denied.");
  }

  @DeleteMapping("/{username}")
//  @CacheEvict(value = {"accounts","account"},allEntries = true)
  public ResponseEntity<String> deleteAccount(@PathVariable String username){

    UserDetails loggedInAccount = getLoggedInAccount();
    if(loggedInAccount.getUsername().equals(username) || isLoggedInAccountAdmin(loggedInAccount)){
      accountService.deleteAccountById(username);
      return ResponseEntity.ok("Account deleted successfully!");
    } else
      throw new AccessDeniedException("Access is denied.");
  }
}
