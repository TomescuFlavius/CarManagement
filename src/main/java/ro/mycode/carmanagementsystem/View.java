package ro.mycode.carmanagementsystem;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ro.mycode.carmanagementsystem.dtos.CarCreateRequest;
import ro.mycode.carmanagementsystem.mappers.CarMapper;
import ro.mycode.carmanagementsystem.model.Car;
import ro.mycode.carmanagementsystem.repository.CarRepository;

import java.util.Scanner;

@Component
public class View {
    private CarRepository carRepository;
    private CarMapper carMapper;
    private Scanner scanner;

    public View(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.scanner = new Scanner(System.in);

        this.play();
    }

    private void play() {
        boolean running = true;
        while (running) {

            System.out.println("1. Adauga o masina");
            System.out.println("2. Afiseaza toate masinile");
            System.out.println("0. Iesire");
            System.out.print("Optiune: ");

            try {
                String input = scanner.nextLine();
                switch (input) {
                    case "1" -> addUserCar();
                    case "2" -> listCars();
                    case "0" -> running = false;
                    default -> System.out.println("Optiune invalida.");
                }
            } catch (Exception e) {
                System.out.println("Eroare neasteptata: " + e.getMessage());
            }
        }
    }

    @Transactional
    public void addUserCar() {
        try {
            System.out.println("\n--- Introduceti datele masinii ---");
            System.out.print("Brand: ");
            String brand = scanner.nextLine();

            System.out.print("Model (minim 3 caractere): ");
            String model = scanner.nextLine();

            System.out.print("An: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Pret: ");
            double price = Double.parseDouble(scanner.nextLine());


            CarCreateRequest request = new CarCreateRequest(brand, model, year, price);

            Car car = carRepository.save(carMapper.toEntity(request));

            System.out.println(" Masina salvata: " + carMapper.toDto(car));
        } catch (NumberFormatException e) {
            System.out.println(" Anul si pretul trebuie sa fie numere!");
        } catch (Exception e) {
            System.out.println("EROARE " + e.getMessage());
        }
    }

    public void listCars() {
        System.out.println("\n--- Toate masinile: ---");
        carRepository.findAll().forEach(car -> System.out.println(carMapper.toDto(car)));
    }
}