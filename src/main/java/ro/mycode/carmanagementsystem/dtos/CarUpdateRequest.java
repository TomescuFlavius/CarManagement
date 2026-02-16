package ro.mycode.carmanagementsystem.dtos;

public record CarUpdateRequest (
        String brand,
        String model,
        double price
        )
{}
