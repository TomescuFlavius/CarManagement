package ro.mycode.carmanagementsystem.dtos;

import lombok.Builder;

@Builder
public record CarResponse(
        Long id,
        String brand,
        String model,
        int year,
        double price
) {}