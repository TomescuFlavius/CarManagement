package ro.mycode.carmanagementsystem.mappers;

import org.springframework.stereotype.Component;
import ro.mycode.carmanagementsystem.dtos.CarCreateRequest;
import ro.mycode.carmanagementsystem.dtos.CarResponse;
import ro.mycode.carmanagementsystem.model.Car;

import java.util.List;

@Component
public class CarMapper {

    public Car toEntity(CarCreateRequest request) {
        if (request == null) return null;

        return Car.builder()
                .brand(request.brand())
                .model(request.model())
                .year(request.year())
                .price(request.price())
                .build();
    }

    public CarResponse toDto(Car car) {
        if (car == null) return null;

        return new CarResponse(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getPrice()
        );
    }

    public List<CarResponse> toDtoList(List<Car> cars) {
        return cars.stream()
                .map(this::toDto)
                .toList();
    }
}