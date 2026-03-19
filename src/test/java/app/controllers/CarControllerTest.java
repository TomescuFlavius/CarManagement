package app.controllers;

import app.dtos.CarCreateRequest;
import app.dtos.CarResponse;
import app.service.CarCommandService;
import app.service.CarQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CarQueryService carQueryService;

    @MockitoBean
    private CarCommandService carCommandService;

    @Test
    void createCarTest() throws Exception {
        CarCreateRequest carCreateRequest = new CarCreateRequest("test", "test", 2022, 2000.0);
        CarResponse carResponse = new CarResponse(1L, "test", "test", 2022, 2000.0);

        when(carCommandService.create(carCreateRequest)).thenReturn(carResponse);

        mockMvc.perform(post("/api/v1/cars/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.brand").value("test"))
                .andExpect(jsonPath("$.model").value("test"))
                .andExpect(jsonPath("$.year").value(2022))
                .andExpect(jsonPath("$.price").value(2000.0));

        verify(carCommandService).create(carCreateRequest);
    }
}
