package app.cars.controllers;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.cars.dtos.CarCreateRequest;
import app.cars.dtos.CarResponse;
import app.cars.dtos.CarResponseList;
import app.cars.dtos.CarUpdateRequest;
import app.cars.service.CarCommandService;
import app.cars.service.CarQueryService;
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
    @PostMapping("/add")
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CarCreateRequest carCreateRequest){
        log.info("Http post /api/v1/cars/ brand={} model={} year={} price={} " ,carCreateRequest.brand(), carCreateRequest.model(), carCreateRequest.year(), carCreateRequest.price());
        return ResponseEntity.status(HttpStatus.CREATED).body(carCommandService.create(carCreateRequest));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id,  @Valid @RequestBody CarUpdateRequest carUpdateRequest){
        log.info("Http put /api/v1/cars/{} brand={} model={} price={} ",id, carUpdateRequest.brand(), carUpdateRequest.model(),carUpdateRequest.price());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(carCommandService.update(id,carUpdateRequest));
    }
    @DeleteMapping("/delete/{model}")
    public ResponseEntity<CarResponse> deleteCar(@PathVariable String model){
        log.info("Http delete /api/v1/cars/delete/{}", model);
        return ResponseEntity.accepted().body( carCommandService.delete(model));
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id){
        log.info("Http get /api/v1/cars/get/{}", id);
        return ResponseEntity.ok(carQueryService.findCarById(id));
    }
}