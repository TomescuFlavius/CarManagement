package app.controllers;

import app.dtos.CarCreateRequest;
import app.dtos.CarResponse;
import app.mappers.CarMapper;
import app.model.Car;
import app.repository.CarRepository;
import app.service.CarCommandService;
import app.service.CarQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers=CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private CarController carController;
    private CarQueryService carQueryService;
    private CarCommandService carCommandService;
    @Autowired
    private CarMapper carMapper;

    @BeforeEach
    void setup(){
        mapper=new ObjectMapper();
        carController=new CarController(carCommandService,carQueryService);
    }

    @Test
    void createCarTest() throws Exception {
        CarCreateRequest carCreateRequest=new CarCreateRequest("test","test",2022,2000.0);
        CarResponse carResponse =new CarResponse(1L,"test","test",2022,2000.0);
        when(carCommandService.create(carCreateRequest)).thenReturn(carResponse);

        MvcResult result= (MvcResult) mockMvc.perform(post("/api/v1/cars/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(carCreateRequest)))
                .andExpect(status().isCreated())
                .andReturn();


    }










}
