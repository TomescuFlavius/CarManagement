package ro.mycode.carmanagementsystem;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ro.mycode.carmanagementsystem.dtos.CarCreateRequest;
import ro.mycode.carmanagementsystem.dtos.CarUpdateRequest;
import ro.mycode.carmanagementsystem.mappers.CarMapper;
import ro.mycode.carmanagementsystem.model.Car;
import ro.mycode.carmanagementsystem.repository.CarRepository;
import ro.mycode.carmanagementsystem.service.CarCommandService;
import ro.mycode.carmanagementsystem.service.CarQueryService;

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

        this.play();
    }

    private void play() {
        boolean running = true;
        while (running) {

            System.out.println("1. Adauga o masina");

            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            try {
                String input = scanner.nextLine();
                switch (input) {
                    case "1" -> this.carCommandService.delete("update");
                    case "2" -> System.out.println(this.carQueryService.findAllCars());
                    case "0" -> running = false;
                    default -> System.out.println("Optiune invalida.");
                }
            } catch (Exception e) {
                System.out.println("Eroare neasteptata: " + e.getMessage());
            }
        }
    }
}