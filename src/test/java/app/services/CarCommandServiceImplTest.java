package app.services;

import app.dtos.CarCreateRequest;
import app.dtos.CarResponse;
import app.dtos.CarUpdateRequest;
import app.exceptions.CarAlreadyExistException;
import app.exceptions.CarNotFoundException;
import app.mappers.CarMapper;
import app.model.Car;
import app.repository.CarRepository;
import app.service.CarCommandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarCommandServiceImplTest {

    @Mock
    private CarRepository carRepository;
    private CarMapper carMapper;
    private CarCommandServiceImpl carCommandService;

    @BeforeEach
    void setup(){
        carMapper= new CarMapper();
        carCommandService= new CarCommandServiceImpl(carRepository,carMapper);
    }

    @Test
    void createThrowCarAlreadyExistException(){
        CarCreateRequest carCreateRequest=new CarCreateRequest("Bmw","Seria5",2007,200.0);
        when(carRepository.existsCarByBrandIgnoreCase(carCreateRequest.brand())).thenReturn(true);
        assertThrows(CarAlreadyExistException.class,()->{carCommandService.create(carCreateRequest);});
    }

    @Test
    void createThrowSavedCar(){
        CarCreateRequest request=new CarCreateRequest("Bmw","Seria5",2007,200.0);
        Car createdCar= new Car(1l,"Bmw","Seria5",2007,200.0);
        Car car=carMapper.toEntity(request);
        when(carRepository.existsCarByBrandIgnoreCase(request.brand())).thenReturn(false);
        when(carRepository.save(car)).thenReturn(createdCar);
        CarResponse carResponse= carCommandService.create(request);
        assertEquals(carMapper.toDto(createdCar),carResponse);
    }

    @Test
    void updateThrowCarNotFound(){
        CarUpdateRequest request=new CarUpdateRequest("Bmw","Bmw2",200.0);
        when(carRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class,()->carCommandService.update(1l, request));
    }

    @Test
    void updateReturn(){
        Car searchedCar= new Car(1l,"Bmw","Seria5",2007,200.0);
        CarUpdateRequest carUpdateRequest = new CarUpdateRequest("Bmw2","Seria51",100);
        Car editedCar= new Car(1l,"Bmw2","Seria51",2007,100.0);
        when(carRepository.findById(1L)).thenReturn(Optional.of(searchedCar));
        when(carRepository.save(editedCar)).thenReturn(editedCar);
        CarResponse response= carCommandService.update(1L,carUpdateRequest);
        assertEquals(response,carMapper.toDto(editedCar));
    }

    @Test
    void deleteThrowCarNotFound(){
        CarUpdateRequest request=new CarUpdateRequest("Bmw","Bmw2",200.0);
        when(carRepository.findCarByModel(request.model())).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class,()->carCommandService.delete(request.model()));
    }

    @Test
    void deleteReturn(){
        Car carOpt=new Car(1l,"Bmw","Seria5",2007,200.0);
        Car foundCar=new Car(1l,"Bmw","Seria5",2007,200.0);
        when(carRepository.findCarByModel(carOpt.getModel())).thenReturn(Optional.of(foundCar));
        CarResponse carResponse=carCommandService.delete(foundCar.getModel());
        assertEquals(carResponse,carMapper.toDto(foundCar));
    }
}
