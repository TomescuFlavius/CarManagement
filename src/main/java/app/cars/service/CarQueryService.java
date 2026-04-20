package app.cars.service;
import app.cars.dtos.CarResponse;
import app.cars.dtos.CarResponseList;

public interface CarQueryService {
    CarResponseList findAllCars();
    CarResponseList findByBrand(String brand);
    CarResponse findCarById(Long id);
}
