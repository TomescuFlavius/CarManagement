package app.controllers;

import app.dtos.CarCreateRequest;
import app.dtos.CarResponse;
import app.dtos.CarUpdateRequest;
import app.repository.CarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CarControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CarRepository carRepository;
    @BeforeEach
    void cleanDatabase(){
        carRepository.deleteAll();
    }
    @Test
    void createGetUpdateDeleteFlow()throws Exception{
        CarCreateRequest carCreateRequest=new CarCreateRequest("Bmw", "Seria3", 2022, 2000.0);

        MvcResult createResult=mockMvc.perform(post("/api/v1/cars/add")
                .contentType((MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(carCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        CarResponse created=objectMapper.readValue(createResult.getResponse().getContentAsByteArray(),CarResponse.class);

        mockMvc.perform(get("/api/v1/cars/get/{id}",created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Bmw"));

        CarUpdateRequest carUpdateRequest=new CarUpdateRequest("Nou","Nou",2000.0);
        mockMvc.perform(put("/api/v1/cars/{id}",created.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carUpdateRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.brand").value("Nou"));

        mockMvc.perform(delete("/api/v1/cars/delete/{model}",created.model()))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/v1/cars/get/{id}",created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.id()));
    }
    @Test
    void duplicateCreateReturnsConflict()throws Exception{
        CarCreateRequest carCreateRequest=new CarCreateRequest("Bmw", "Seria3", 2022, 2000.0);
        mockMvc.perform(post("/api/v1/cars/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carCreateRequest)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/cars/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carCreateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Masina cu acest brand exista deja!"));
    }
    @Test
    void searchEndpointReturnsFilteredResults() throws Exception{
        CarCreateRequest Bmw=new CarCreateRequest("Bmw", "Seria3", 2022, 2000.0);
        CarCreateRequest Audi=new CarCreateRequest("Audi", "A33", 2022, 2000.0);
        mockMvc.perform(post("/api/v1/cars/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Bmw)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/cars/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Audi)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/cars/all")
                .param("brand","BMW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
    @Test
    void validationErrorsAreReturned() throws Exception {
        CarCreateRequest invalid = new CarCreateRequest(
                "", "", 1999, 0.0
        );

        mockMvc.perform(post("/api/v1/cars/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void globalExceptionHandlerHandlesGenericRuntime() throws Exception {
        var result = mockMvc.perform(get("/api/v1/cars/all")
                        .param("minYear", "2027")
                        .param("maxYear", "2019"))
                .andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(result.getResponse().getContentAsString()).contains("INVALID_REQUEST");
    }
}
