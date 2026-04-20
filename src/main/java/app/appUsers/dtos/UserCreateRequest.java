package app.appUsers.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UserCreateRequest (
        @Size(min = 3, max = 50, message = "Size intre 3-50")
        String name,

        @Email
        @NotBlank(message = "Email obligatoriu")
        String email,

        @NotBlank
        String password
){
}
