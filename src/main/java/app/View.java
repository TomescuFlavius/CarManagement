package app;

import org.springframework.stereotype.Component;
import app.mappers.CarMapper;
import app.repository.CarRepository;
import app.service.CarCommandService;
import app.service.CarQueryService;

import java.util.Scanner;

@Component
public class View {
    private CarRepository carRepository;
    private CarMapper carMapper;
    private CarCommandService carCommandService;
    private CarQueryService carQueryService;
    private Scanner scanner;

    public View(CarRepository carRepository, CarMapper carMapper, CarQueryService carQueryService, CarCommandService carCommandService) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.carCommandService=carCommandService;
        this.carQueryService=carQueryService;
        this.scanner = new Scanner(System.in);


    }
}