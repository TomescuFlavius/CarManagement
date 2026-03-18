package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Entity
@Table(name = "cars")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Brand-ul este obligatoriu")
    private String brand;

    @Size(min = 3, message = "Modelul trebuie să aibă minim 3 caractere")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Modelul poate conține litere, cifre și spații")
    private String model;


    private Integer year;
    private double price;
}