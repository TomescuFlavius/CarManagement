package app.service;

import app.dtos.CarCreateRequest;
import app.dtos.CarResponse;
import app.dtos.CarUpdateRequest;

public interface CarCommandService {
    CarResponse create(CarCreateRequest request);
    CarResponse update(long id, CarUpdateRequest request);
    CarResponse delete(String model);
}
