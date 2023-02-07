package net.igap.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import net.igap.exception.APIException;
import net.igap.exception.NationalCodeException;
import net.igap.model.dtos.ResponseDtoWithPagination;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class Helpers {

  public static LocalDate validateDate(String dateInput) {
    try {
      LocalDate dateEntered = LocalDate.parse(dateInput);
      if (dateEntered.isAfter(LocalDate.parse(AppConstants.MIN_VALID_DATE)))
        return dateEntered;

      throw new APIException("Invalid Date input. Date must be after 1950.");

    }catch (DateTimeParseException e){
      log.warn("Date input is invalid: " + dateInput);
      throw new APIException("Invalid Date format: "+ dateInput);
    }
  }

  public static String validateGender(String gender) {
    String genderInput = gender.trim().toLowerCase();
    return switch (genderInput) {
      case AppConstants.MALE -> AppConstants.MALE;
      case AppConstants.FEMALE -> AppConstants.FEMALE;
      default ->
          throw new APIException("Invalid Gender input (male,female)");
    };
  }

  public static Long validateFixLengthDigitNationalCode(String nationalCode) {
    try {
      Long convertedToLong = Long.parseLong(nationalCode);

      if (nationalCode.matches("^\\d{10}$"))
        return convertedToLong;

      throw new NationalCodeException("National code must be 10 digit.");

    } catch (NumberFormatException e) {
      log.warn("National code input is invalid: " + nationalCode);
      throw new APIException("National code input is invalid: " + nationalCode);
    }
  }


  public static String generateRandomString(int length){
    String possibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < length; i += 1) {
      int position = (int) Math.floor(Math.random() * possibleChars.length());
      result.append(possibleChars.charAt(position));
    }

    return result.toString();
  }

  public static Long generateRandomNumber(){
     return ThreadLocalRandom.current().nextLong(1_000_000_000, 1_999_999_999);
  }

  public static UserDetails getLoggedInAccount(){
   return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public static boolean isLoggedInAccountAdmin(UserDetails customUserDetail){
    return customUserDetail.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
  }

  public static <dto> ResponseDtoWithPagination<dto> mapToDtoResponseWithPagination(
      Page<?> entities, List<dto> contents){
    ResponseDtoWithPagination<dto> dtoResponse = new ResponseDtoWithPagination<>();
    dtoResponse.setContent(contents);
    dtoResponse.setPageNo(entities.getNumber());
    dtoResponse.setPageSize(entities.getSize());
    dtoResponse.setTotalElements(entities.getTotalElements());
    dtoResponse.setTotalPages(entities.getTotalPages());
    dtoResponse.setLast(entities.isLast());
    return dtoResponse;
  }

}
