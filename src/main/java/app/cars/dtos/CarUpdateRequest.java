package app.cars.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CarUpdateRequest (
        @NotBlank(message = "Brand-ul este obligatoriu")
        @Size(min = 3, max = 50, message = "Brand-ul trebuie sa aiba intre 3 si 50 caractere")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Brand-ul poate contine doar litere")
        String brand,

        @NotBlank(message = "Modelul este obligatoriu")
        String model,

        @Positive(message = "Pretul trebuie sa fie un numar pozitiv")
        double price
        )
{}
