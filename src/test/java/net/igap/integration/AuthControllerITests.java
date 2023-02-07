package net.igap.integration;

import static net.igap.utils.Helpers.generateRandomNumber;
import static net.igap.utils.Helpers.generateRandomString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.igap.model.dtos.CreateAccountDto;
import net.igap.model.dtos.LoginDto;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerITests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Order(1)
  public void itShouldRegisterNewAccountSuccessfully() throws Exception{

    // given
    CreateAccountDto accountDto = new CreateAccountDto();
    accountDto.setFirstName("Majid");
    accountDto.setLastName("Salari");
    accountDto.setDateOfBirth("1992-11-01");
    accountDto.setNationalCode(generateRandomNumber().toString());
    accountDto.setGender("male");
    accountDto.setAddressInfo("Iran-Kerman-Motahari");
    accountDto.setProvinceName("kerman");
    accountDto.setCityName("bam");
    accountDto.setUserName(generateRandomString(6));
    accountDto.setPassword("password");

    // when
    ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(accountDto)));

    // then
    response.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  @Order(2)
  public void itShouldReturnJwtToken() throws Exception{

    // given
    LoginDto loginDto = new LoginDto();
    loginDto.setUsername("user");
    loginDto.setPassword("user");

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginDto)));

    //then
    result.andDo(print()).andExpect(status().isOk());
  }
}
