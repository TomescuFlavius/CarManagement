package app.service;
import app.dtos.CarResponse;
import app.dtos.CarResponseList;

public interface CarQueryService {
    CarResponseList findAllCars();
    CarResponseList findByBrand(String brand);
    CarResponse findCarById(Long id);
}
