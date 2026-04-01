package app.service;
import app.dtos.CarResponse;
import app.exceptions.CarNotFoundException;
import org.springframework.stereotype.Service;
import app.dtos.CarResponseList;
import app.mappers.CarMapper;
import app.repository.CarRepository;
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
        return new CarResponseList(carMapper.toDtoList(carRepository.findCarByBrand(brand)));
    }
    @Override
    public CarResponse findCarById(Long id) {
        if (carRepository.findCarById(id).isEmpty()) throw new CarNotFoundException();
        return carMapper.toDto(carRepository.findCarById(id).get());
    }
}
