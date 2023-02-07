package net.igap.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.igap.model.dtos.CityDto;
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
public class CityControllerITest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Order(1)
  @WithMockUser(roles = "ADMIN")
  public void giveAdminAccount_whenGetAllCities_thenReturnAllProvinceRelatedCities() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/provinces/{provinceId}/cities",2)
            .accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  @Order(2)
  @WithMockUser(roles = "USER")
  public void giveUserAccount_whenGetAllCities_thenReturnForbidden() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/provinces/{provinceId}/cities",2)
            .accept(MediaType.ALL))
        .andExpect(status().isForbidden());
  }

  @Test
  @Order(3)
  @WithMockUser(roles = "ADMIN")
  public void itShouldRegisterNewCityForAProvinceSuccessfully() throws Exception{

    // given
    CityDto cityDto = new CityDto();
    cityDto.setCityName("shahriar");

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/provinces/{provinceId}/cities",2)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(cityDto)));

    //then
    result.andDo(print()).andExpect(status().isCreated());
  }

  @Test
  @Order(4)
  @WithMockUser(roles = "ADMIN")
  public void giveAdminAccount_whenGetACityFromAProvince_thenReturnOk() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.
            get("/api/v1/provinces/{provinceId}/cities/{cityId}",1,1)
            .accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  @Order(5)
  @WithMockUser(roles = "ADMIN")
  public void giveAdminAccount_whenUpdateCityFromAProvince_thenReturnOk() throws Exception {
    // given
    CityDto cityDto = new CityDto();
    cityDto.setCityName("damavand");

    // when
    ResultActions result = mockMvc.perform(put("/api/v1/provinces/{provinceId}/cities/{cityId}",2,4)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(cityDto)));

    //then
    result.andDo(print()).andExpect(status().isOk());
  }

  @Test
  @Order(6)
  @WithMockUser(roles = "ADMIN")
  public void giveAdminAccount_whenDeleteAProvince_thenReturnOk() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/provinces/{provinceId}/cities/{cityId}",2,3)
            .accept(MediaType.ALL))
        .andExpect(status().isOk());
  }



}
