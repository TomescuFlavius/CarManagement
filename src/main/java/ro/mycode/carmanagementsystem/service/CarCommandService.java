package ro.mycode.carmanagementsystem.service;

import ro.mycode.carmanagementsystem.dtos.CarCreateRequest;
import ro.mycode.carmanagementsystem.dtos.CarResponse;
import ro.mycode.carmanagementsystem.dtos.CarUpdateRequest;

public interface CarCommandService {
    CarResponse create(CarCreateRequest request);
    CarResponse update(long id, CarUpdateRequest request);
    void delete(String model);
}
