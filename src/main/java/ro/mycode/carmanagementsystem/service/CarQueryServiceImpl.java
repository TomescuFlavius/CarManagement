package ro.mycode.carmanagementsystem.service;

import org.springframework.stereotype.Service;
import ro.mycode.carmanagementsystem.dtos.CarResponseList;
import ro.mycode.carmanagementsystem.mappers.CarMapper;
import ro.mycode.carmanagementsystem.repository.CarRepository;

@Service
public class CarQueryServiceImpl implements CarQueryService{

    private CarRepository carRepository;
    private CarMapper carMapper;

    public CarQueryServiceImpl(CarRepository carRepository, CarMapper carMapper){
        this.carMapper=carMapper;
        this.carRepository=carRepository;
    }



    @Override
    public CarResponseList findAllCars() {
        return new CarResponseList(carMapper.toDtoList(carRepository.findAll()));
    }

    @Override
    public CarResponseList findByBrand(String brand) {
        return new CarResponseList(carMapper.toDtoList(carRepository.findCarByBrand(brand)));
    }
}
