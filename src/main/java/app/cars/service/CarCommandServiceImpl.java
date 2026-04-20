package app.cars.service;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import app.cars.dtos.CarCreateRequest;
import app.cars.dtos.CarResponse;
import app.cars.dtos.CarUpdateRequest;
import app.cars.exceptions.CarAlreadyExistException;
import app.cars.exceptions.CarNotFoundException;
import app.cars.mappers.CarMapper;
import app.cars.model.Car;
import app.cars.repository.CarRepository;
@Service
public class CarCommandServiceImpl implements CarCommandService{
    private CarRepository carRepository;
    private CarMapper carMapper;
    public CarCommandServiceImpl(CarRepository carRepository,CarMapper carMapper){
        this.carRepository=carRepository;
        this.carMapper=carMapper;
    }
    @Override
    @Transactional
    public CarResponse create(CarCreateRequest request) {
        if(carRepository.existsCarByBrandIgnoreCase(request.brand())) {
          throw new CarAlreadyExistException();
        }
        Car savedCar=carRepository.save(carMapper.toEntity(request));
        return carMapper.toDto(savedCar);
    }
    @Override
    @Transactional
    public CarResponse update(long id, CarUpdateRequest request) {
        Car car=carRepository.findById(id).orElseThrow(CarNotFoundException::new);
            car.setBrand(request.brand());
            car.setModel(request.model());
            car.setPrice(request.price());
        Car updatedCar=carRepository.save(car);
        return carMapper.toDto(updatedCar);
    }
    @Override
    @Transactional
    public CarResponse delete(String model) {
       Car carOpt= carRepository.findCarByModel(model).orElseThrow(CarNotFoundException::new);
       carRepository.delete(carOpt);
       return carMapper.toDto(carOpt);
    }
}
