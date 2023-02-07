package net.igap.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.igap.model.dtos.UpdateDto;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerITests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  @Order(1)
  @WithMockUser(roles = "ADMIN")
  public void giveAdminAccount_whenGetAllAccounts_thenReturnAllAccounts() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts").accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  @Order(2)
  @WithMockUser(roles = "USER")
  public void giveUserAccount_whenGetAllAccounts_thenReturnForbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts")
            .accept(MediaType.ALL))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(3)
  @WithMockUser(username = "user", roles = "USER")
  public void giveUserAccount_whenGetAccount_thenReturnUserAccount() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/{username}", "user")
            .accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  @Order(4)
  @WithMockUser(username = "user", roles = "USER")
  public void giveUserAccount_whenGetAnotherAccount_thenReturnForbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/{username}", "user2")
            .accept(MediaType.ALL))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(5)
  @WithMockUser(username = "admin", roles = "ADMIN")
  public void giveAdminAccount_whenGetAnyAccount_thenReturnUserAccount() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/{username}", "user")
            .accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  @Order(6)
  @WithMockUser(username = "user3", roles = "USER")
  public void giveUserAccount_whenUpdateAccount_thenReturnOk() throws Exception {

    // given
    UpdateDto updateDto = new UpdateDto();
    updateDto.setAddressInfo("updateToKerman");
    updateDto.setProvinceName("kerman");
    updateDto.setCityName("zarand");
    updateDto.setPassword("user3update");

    // when
    ResultActions result = mockMvc.perform(put("/api/v1/accounts/{username}", "user3")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDto)));

    //then
    result.andDo(print()).andExpect(status().isOk());
  }

  @Test
  @Order(7)
  @WithMockUser(username = "user2", roles = "USER")
  public void giveUserAccount_whenUpdateAnotherAccount_thenReturnForbidden() throws Exception {

    // given
    UpdateDto updateDto = new UpdateDto();
    updateDto.setAddressInfo("updateToKerman");
    updateDto.setProvinceName("kerman");
    updateDto.setCityName("zarand");
    updateDto.setPassword("user3update");

    // when
    ResultActions result = mockMvc.perform(put("/api/v1/accounts/{username}", "user3")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDto)));

    //then
    result.andDo(print()).andExpect(status().isForbidden());
  }

  @Test
  @Order(8)
  @WithMockUser(username = "admin", roles = "ADMIN")
  public void giveAdminAccount_whenUpdateAnotherAccount_thenReturnOk() throws Exception {

    // given
    UpdateDto updateDto = new UpdateDto();
    updateDto.setAddressInfo("updateToKerman");
    updateDto.setProvinceName("kerman");
    updateDto.setCityName("zarand");
    updateDto.setPassword("user3update");

    // when
    ResultActions result = mockMvc.perform(put("/api/v1/accounts/{username}", "user3")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDto)));

    //then
    result.andDo(print()).andExpect(status().isOk());
  }

  @Test
  @Order(9)
  @WithMockUser(username = "user3", roles = "USER")
  public void giveUserAccount_whenDeleteAccount_thenReturnOk() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/{username}", "user3")
            .accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  @Order(10)
  @WithMockUser(username = "user2", roles = "USER")
  public void giveUserAccount_whenDeleteAnotherAccount_thenReturnForbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/{username}", "user3")
            .accept(MediaType.ALL))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(11)
  @WithMockUser(username = "admin", roles = "ADMIN")
  public void giveAdminAccount_whenDeleteAnotherAccount_thenReturnok() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/accounts/{username}", "user2")
            .accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

}
