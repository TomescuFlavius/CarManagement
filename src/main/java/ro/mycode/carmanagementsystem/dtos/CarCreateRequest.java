package ro.mycode.carmanagementsystem.dtos;

import jakarta.validation.constraints.*;

public record CarCreateRequest(
        @NotBlank(message = "Brand-ul este obligatoriu")
        @Size(min = 3, max = 50, message = "Brand-ul trebuie sa aiba intre 3 si 50 caractere")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Brand-ul poate contine doar litere")
        String brand,

        @NotBlank(message = "Modelul este obligatoriu")
        String model,

        @Min(value = 1886, message = "Anul trebuie sa fie valid (dupa inventarea primei masini)")
        @Max(value = 2026, message = "Anul nu poate fi in viitor")
        int year,

        @Positive(message = "Pretul trebuie sa fie un numar pozitiv")
        double price
) {}