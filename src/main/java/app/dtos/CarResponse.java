package app.dtos;

import lombok.Builder;

@Builder
public record CarResponse(
        Long id,
        String brand,
        String model,
        int year,
        double price
) {}