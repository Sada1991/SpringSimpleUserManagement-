package net.igap.service;


import static net.igap.utils.Helpers.validateFixLengthDigitNationalCode;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.igap.exception.NationalCodeException;
import net.igap.exception.ResourceNotFoundException;
import net.igap.model.dtos.CreateAccountDto;
import net.igap.model.dtos.CreateAccountResponseDto;
import net.igap.model.dtos.GetAccountDto;
import net.igap.model.dtos.ResponseDtoWithPagination;
import net.igap.model.dtos.UpdateDto;
import net.igap.model.entity.Account;
import net.igap.model.entity.Address;
import net.igap.model.entity.City;
import net.igap.model.entity.Person;
import net.igap.model.entity.Province;
import net.igap.model.entity.Role;
import net.igap.repository.AccountRepository;
import net.igap.repository.RoleRepository;
import net.igap.service.interfaces.AccountService;
import net.igap.service.interfaces.AddressService;
import net.igap.service.interfaces.PersonService;
import net.igap.service.interfaces.ProvinceService;
import net.igap.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final PersonService personService;
  private final AddressService addressService;
  private final ProvinceService provinceService;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;


  @Autowired
  public AccountServiceImpl(AccountRepository accountRepository,
      PersonService personService, AddressService addressService, ProvinceService provinceService,
      RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
    this.accountRepository = accountRepository;
    this.personService = personService;
    this.addressService = addressService;
    this.provinceService = provinceService;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public CreateAccountResponseDto createAccount(CreateAccountDto accountDto) {
    checkNationalCodeExists(validateFixLengthDigitNationalCode(accountDto.getNationalCode()));
    checkUsernameExists(accountDto.getUserName());
    Person person = personService.savePerson(accountDto);
    Address address = addressService.saveAddress(accountDto);
    Account account = mapToEntity(accountDto, person, address);
    Account newAccount = accountRepository.save(account);
    return mapToAccountResponseDto(newAccount);
  }

  private void checkUsernameExists(String userName) {
    Optional<Account> foundedAccount =
        accountRepository.findAccountByEnableIsTrueAndUserName(userName);
    if(foundedAccount.isPresent()) {
      throw new NationalCodeException(
          String.format("Username [%s] is already exists", userName));
    }
  }

  @Override
  public ResponseDtoWithPagination<GetAccountDto> getAllAccounts(int pageNo, int pageSize) {

    Pageable pageable = PageRequest.of(pageNo,pageSize);
    Page<Account> accounts = accountRepository.findAllByEnableIsTrue(pageable);
    List<GetAccountDto> contents = accounts.getContent().stream()
        .map(this::mapToGetAccountDto).toList();

    return Helpers.mapToDtoResponseWithPagination(accounts, contents);
  }


  @Override
  public GetAccountDto getAccountByUsername(String username) {
    Account foundedAccount = findAccountByUsername(username);
    return mapToGetAccountDto(foundedAccount);
  }

  @Override
  public Account findAccountByUsername(String username) {
    return accountRepository.findAccountByEnableIsTrueAndUserName(username)
        .orElseThrow(()->new ResourceNotFoundException(
            String.format("Account not found with username : '%s'", username)));
  }

  @Override
  public Account findAccountById(Long accountId) {
    return accountRepository.findAccountByEnableIsTrueAndId(accountId)
        .orElseThrow(()->new ResourceNotFoundException(
            String.format("Account not found with id : '%s'", accountId)));
  }

  @Override
  public void checkNationalCodeExists(Long nationalCode) {
    Optional<Account> foundedAccount =
        accountRepository.findAccountByEnableIsTrueAndPersonNationalCode(nationalCode);
    if(foundedAccount.isPresent()) {
        throw new NationalCodeException(
            String.format("National code [%s] is already exists", nationalCode));
    }
  }

  @Override
  @Transactional
  public GetAccountDto updateAccount(UpdateDto accountDto, String username) {
    Account foundedAccount = findAccountByUsername(username);

    if(accountDto.getAddressInfo() != null && !accountDto.getAddressInfo().trim().equals(""))
      foundedAccount.getAddress().setAddressInfo(accountDto.getAddressInfo());
    if(accountDto.getProvinceName() != null)
      updateProvince(accountDto, foundedAccount);
    if(accountDto.getCityName() != null)
      updateCity(accountDto, foundedAccount);
    if(accountDto.getPassword() != null && !accountDto.getPassword().trim().equals(""))
      foundedAccount.setPassword(passwordEncoder.encode(accountDto.getPassword()));

    Account updatedAccount = accountRepository.save(foundedAccount);
    return mapToGetAccountDto(updatedAccount);
  }

  @Override
  public void deleteAccountById(String username) {
    Account foundedAccount = findAccountByUsername(username);
    foundedAccount.setEnable(false);
    accountRepository.save(foundedAccount);
  }

  private void updateCity(UpdateDto accountDto, Account account) {
    City city = provinceService.checkCityBelongsToProvince(accountDto, account.getAddress()
        .getProvince());
    account.getAddress().setCity(city);
  }

  private void updateProvince(UpdateDto accountDto, Account account) {
      Province province = provinceService.checkProvinceExistsByName(accountDto.getProvinceName());
      City city = provinceService.checkCityBelongsToProvince(accountDto, province);
      account.getAddress().setProvince(province);
      account.getAddress().setCity(city);
  }

  private Account mapToEntity(CreateAccountDto accountDto, Person person, Address address) {

    Account account = new Account();

    account.setPerson(person);
    account.setAddress(address);
    account.setUserName(accountDto.getUserName());
    account.setPassword(passwordEncoder.encode(accountDto.getPassword()));

    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName("ROLE_USER").get();
    roles.add(userRole);
    account.setRoles(roles);

    return account;
  }


  private GetAccountDto mapToGetAccountDto(Account account) {

    GetAccountDto accountDto = new GetAccountDto();

    accountDto.setId(account.getId());
    accountDto.setUserName(account.getUserName());
    accountDto.setFirstName(account.getPerson().getFirstName());
    accountDto.setLastName(account.getPerson().getLastName());
    accountDto.setNationalCode(account.getPerson().getNationalCode());
    accountDto.setGender(account.getPerson().getGender());
    accountDto.setDateOfBirth(account.getPerson().getDateOfBirth().toString());
    accountDto.setAddressInfo(account.getAddress().getAddressInfo());
    accountDto.setCityName(account.getAddress().getCity().getCityName());
    accountDto.setProvinceName(account.getAddress().getProvince().getProvinceName());

    return accountDto;
  }

  private CreateAccountResponseDto mapToAccountResponseDto(Account account) {

    CreateAccountResponseDto accountDto = new CreateAccountResponseDto();

    accountDto.setId(account.getId());
    accountDto.setUserName(account.getUserName());
    accountDto.setFirstName(account.getPerson().getFirstName());
    accountDto.setLastName(account.getPerson().getLastName());

    return accountDto;
  }
}
