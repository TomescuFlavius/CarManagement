package app.cars.service;
import app.cars.dtos.CarResponse;
import app.cars.exceptions.CarNotFoundException;
import org.springframework.stereotype.Service;
import app.cars.dtos.CarResponseList;
import app.cars.mappers.CarMapper;
import app.cars.repository.CarRepository;
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
    public CarResponseList findByBrand(String brand){
        return new CarResponseList(carMapper.toDtoList(carRepository.findCarByBrandIgnoreCase(brand)));
    }
    @Override
    public CarResponse findCarById(Long id) {
        if (carRepository.findCarById(id).isEmpty()) throw new CarNotFoundException();
        return carMapper.toDto(carRepository.findCarById(id).get());
    }
}
