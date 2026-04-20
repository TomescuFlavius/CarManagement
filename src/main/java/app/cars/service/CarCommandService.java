package app.cars.service;
import app.cars.dtos.CarCreateRequest;
import app.cars.dtos.CarResponse;
import app.cars.dtos.CarUpdateRequest;
public interface CarCommandService {
    CarResponse create(CarCreateRequest request);
    CarResponse update(long id, CarUpdateRequest request);
    CarResponse delete(String model);
}
