package ro.mycode.carmanagementsystem.repository;

import ro.mycode.carmanagementsystem.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

}