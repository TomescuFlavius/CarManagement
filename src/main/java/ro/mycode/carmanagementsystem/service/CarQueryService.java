package ro.mycode.carmanagementsystem.service;

import ro.mycode.carmanagementsystem.dtos.CarResponse;
import ro.mycode.carmanagementsystem.dtos.CarResponseList;

public interface CarQueryService {

    CarResponseList findAllCars();
    CarResponseList findByBrand(String brand);

}
