package app.services;
import app.dtos.CarResponseList;
import app.mappers.CarMapper;
import app.model.Car;
import app.repository.CarRepository;
import app.service.CarQueryService;
import app.service.CarQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarQueryServiceImplTest {
    @Mock
    private CarRepository carRepository;
    private CarMapper carMapper;
    private CarQueryService carQueryService;
    @BeforeEach
    void setup(){
        carMapper=new CarMapper();
        carQueryService=new CarQueryServiceImpl(carRepository,carMapper);
    }
    @Test
    void findAllCarsTest(){
        Car car1 = new Car(1L, "Audi", "A4", 2020, 300.0);
        Car car2 = new Car(2L, "Dacia", "Logan", 2022, 100.0);
        List<Car> cars = List.of(car1, car2);
        when(carRepository.findAll()).thenReturn(cars);
        CarResponseList response = carQueryService.findAllCars();
        assertEquals(carMapper.toDtoList(cars), response.carResponseList());
    }
    @Test
    void findByBrand(){
        Car car1 = new Car(1L, "Audi", "A4", 2020, 300.0);
        Car car2=new Car(2L, "Audi", "A4", 2020, 300.0);
        List<Car> goodCars=List.of(car1,car2);
        when(carRepository.findCarByBrand(car1.getBrand())).thenReturn(goodCars);
        CarResponseList responseList=carQueryService.findByBrand(car1.getBrand());
        assertEquals(carMapper.toDtoList(goodCars), responseList.carResponseList());
    }
}
