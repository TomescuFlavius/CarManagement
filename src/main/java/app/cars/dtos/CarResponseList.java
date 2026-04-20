package app.cars.dtos;

import java.util.List;

public record CarResponseList (
        List<CarResponse> carResponseList
)
{
}
