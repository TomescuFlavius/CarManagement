package app.controllers;

import app.cars.dtos.CarCreateRequest;
import app.cars.dtos.CarResponse;
import app.cars.dtos.CarUpdateRequest;
import app.cars.repository.CarRepository;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
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
                .with(jwt().authorities(() -> "Car:Write"))
                .contentType((MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(carCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        CarResponse created=objectMapper.readValue(createResult.getResponse().getContentAsByteArray(),CarResponse.class);

        mockMvc.perform(get("/api/v1/cars/get/{id}",created.id())
                .with(jwt().authorities(() -> "Car:Read")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Bmw"));

        CarUpdateRequest carUpdateRequest=new CarUpdateRequest("Nou","Nou",2000.0);
        mockMvc.perform(put("/api/v1/cars/{id}",created.id())
                .with(jwt().authorities(() -> "Car:Write"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carUpdateRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.brand").value("Nou"));

        mockMvc.perform(delete("/api/v1/cars/delete/{model}","Nou")
                .with(jwt().authorities(() -> "Car:Write")))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/v1/cars/get/{id}",created.id())
                .with(jwt().authorities(() -> "Car:Read")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Masina nu a fost gasita!"));
    }
    @Test
    void duplicateCreateReturnsConflict()throws Exception{
        CarCreateRequest carCreateRequest=new CarCreateRequest("Bmw", "Seria3", 2022, 2000.0);
        mockMvc.perform(post("/api/v1/cars/add")
                .with(jwt().authorities(() -> "Car:Write"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carCreateRequest)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/cars/add")
                .with(jwt().authorities(() -> "Car:Write"))
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
                        .with(jwt().authorities(() -> "Car:Write"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Bmw)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/cars/add")
                        .with(jwt().authorities(() -> "Car:Write"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Audi)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/cars/all")
                .with(jwt().authorities(() -> "Car:Read"))
                .param("brand","BMW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carResponseList.length()").value(1));
    }
    @Test
    void validationErrorsAreReturned() throws Exception {
        CarCreateRequest invalid = new CarCreateRequest(
                "", "", 1999, 0.0
        );

        mockMvc.perform(post("/api/v1/cars/add")
                        .with(jwt().authorities(() -> "Car:Write"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

}
