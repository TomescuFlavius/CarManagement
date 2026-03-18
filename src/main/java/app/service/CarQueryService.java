package app.service;

import app.dtos.CarResponseList;

public interface CarQueryService {

    CarResponseList findAllCars();
    CarResponseList findByBrand(String brand);

}
