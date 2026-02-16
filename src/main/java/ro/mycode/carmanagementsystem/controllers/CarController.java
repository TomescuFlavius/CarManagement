package ro.mycode.carmanagementsystem.controllers;

import lombok.extern.slf4j.Slf4j;
import ro.mycode.carmanagementsystem.dtos.CarResponseList;
import ro.mycode.carmanagementsystem.service.CarCommandService;
import ro.mycode.carmanagementsystem.service.CarQueryService;
import ro.mycode.carmanagementsystem.service.CarQueryServiceImpl;

@RestController
@RequestMapping("/api/v1/cars")
@Slf4j
public class CarController {

    private CarQueryService carQueryService;
    private CarCommandService carCommandService;

    public CarController(CarCommandService carCommandService, CarQueryService carQueryService){
        this.carCommandService=carCommandService;
        this.carQueryService =carQueryService;
    }

    @GetMapping("/all")
    public ResponseEntity<CarResponseList> getAllCars(){
        log.info("Http  get  /api/v1/cars/all");
        return ResponseEntity.ok(carQueryService.findAllCars());
    }


}
