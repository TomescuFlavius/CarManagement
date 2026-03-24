package app.controllers;
import app.dtos.CarCreateRequest;
import app.dtos.CarResponse;
import app.dtos.CarResponseList;
import app.dtos.CarUpdateRequest;
import app.exceptions.CarNotFoundException;
import app.service.CarCommandService;
import app.service.CarQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Test
    void getAllTest() throws Exception {
        CarResponse carResponse=new CarResponse(1L,"br","mo",2020,2000.0);
        CarResponse carResponse1=new CarResponse(2L,"bra","mod",2021,3000.0);
        CarResponse carResponse2=new CarResponse(3L,"bran","mode",2022,4000.0);
        List<CarResponse> list=new ArrayList<>();
        list.add(carResponse);
        list.add(carResponse1);
        list.add(carResponse2);
        CarResponseList carResponseList=new CarResponseList(list);
        when(carQueryService.findAllCars()).thenReturn(carResponseList);
        mockMvc.perform(get("/api/v1/cars/all"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.carResponseList.length()").value(3))
                .andExpect( jsonPath("$.carResponseList[0].id").value(1L))
                .andExpect(jsonPath("$.carResponseList[1].year").value(2021));
        verify(carQueryService).findAllCars();
    }
    @Test
    void deleteCarTest() throws Exception {
        String model="model";
        CarResponse expectedCarResponse=new CarResponse(1L,"br","model",2020,2000.0);
        when(carCommandService.delete(model)).thenReturn(expectedCarResponse);
        mockMvc.perform(delete("/api/v1/cars/delete/{model}",model))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.brand").value("br"))
                .andExpect(jsonPath("$.model").value("model"))
                .andExpect(jsonPath("$.year").value(2020))
                .andExpect(jsonPath("$.price").value(2000.0));
        verify(carCommandService).delete(model);
    }
    @Test
    void updateCarTest() throws Exception{
        long id=1L;
        CarUpdateRequest carUpdateRequest=new CarUpdateRequest("brand","model",2000.0);
        CarResponse actualCar=new CarResponse(1L,"brand","model",2020,2000.0);
        when(carCommandService.update(id,carUpdateRequest)).thenReturn(actualCar);
        mockMvc.perform(put("/api/v1/cars/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(carUpdateRequest)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.brand").value("brand"))
                .andExpect(jsonPath("$.model").value("model"))
                .andExpect(jsonPath("$.year").value(actualCar.year()))
                .andExpect(jsonPath("$.price").value(2000.0));
        verify(carCommandService).update(1L,carUpdateRequest);
    }
    @Test
    void deleteCarReturnsConflictWhenServiceThrowsNotFound() throws Exception {
        doThrow(new CarNotFoundException()).when(carCommandService).delete("Ghost");

        mockMvc.perform(delete("/api/v1/cars/delete/{model}", "Ghost"))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").value("Masina nu a fost gasita!"));
    }

}
