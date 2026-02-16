package ro.mycode.carmanagementsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ro.mycode.carmanagementsystem.dtos.CarCreateRequest;
import ro.mycode.carmanagementsystem.dtos.CarResponse;
import ro.mycode.carmanagementsystem.dtos.CarUpdateRequest;
import ro.mycode.carmanagementsystem.exceptions.CarAlreadyExistException;
import ro.mycode.carmanagementsystem.exceptions.CarNotFoundException;
import ro.mycode.carmanagementsystem.mappers.CarMapper;
import ro.mycode.carmanagementsystem.model.Car;
import ro.mycode.carmanagementsystem.repository.CarRepository;

import java.util.Optional;

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
        Optional<Car> car=carRepository.findById(id);
        if (!carRepository.existsCarById(car.get().getId())){
            throw new CarNotFoundException();
        }
        if (request.brand()!=null && !request.brand().isBlank()){
            car.get().setBrand(request.brand());
        }
        if (request.model()!=null && !request.model().isBlank()){
            car.get().setModel(request.model());
        }
        if (request.price()>0){
            car.get().setPrice(request.price());
        }
        Car updatedCar=carRepository.save(car.get());
        return carMapper.toDto(updatedCar);
    }

    @Override
    @Transactional
    public void delete(String model) {
        Optional<Car> car=carRepository.findCarByModel(model);
        if (!carRepository.existsCarByBrandIgnoreCase(car.get().getBrand())) throw new CarNotFoundException();
        carRepository.delete(car.get());
    }
}
