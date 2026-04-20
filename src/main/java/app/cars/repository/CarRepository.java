package app.cars.repository;
import org.springframework.data.repository.query.Param;
import app.cars.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findCarByBrand(@Param("brand") String brand);
    boolean existsCarById(@Param("id") Long id);
    boolean existsCarByBrandIgnoreCase(@Param("brand") String brand);
    boolean existsCarByModelIgnoreCase(@Param("model") String model);
    Optional<Car> findCarByModel(@Param("model") String model);
    Optional<Car> findCarById(Long id);
}