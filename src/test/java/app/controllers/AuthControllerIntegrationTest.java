package app.controllers;

import app.appUsers.dtos.UserCreateRequest;
import app.appUsers.repository.AppUserRepository;
import app.auth.dtos.AuthLoginRequest;
import app.auth.dtos.AuthResponse;
import app.cars.dtos.CarCreateRequest;
import app.cars.dtos.CarResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        appUserRepository.deleteAll();
    }


    @Test
    void registerAndLoginFlow()  throws Exception {
        UserCreateRequest request=new UserCreateRequest("Nume", "email@gmail.com", "password");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("email@gmail.com"));

        AuthLoginRequest loginRequest=new AuthLoginRequest("email@gmail.com", "password");

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email@gmail.com"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void duplicateRegisterConflict() throws Exception {
        UserCreateRequest request=new UserCreateRequest("Nume", "email@gmail.com", "password");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exists"));
    }


    @Test
    void invalidLoginReturnsUnauthorized() throws Exception {
        UserCreateRequest request=new UserCreateRequest("Nume", "email@gmail.com", "password");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        AuthLoginRequest loginRequest=new AuthLoginRequest("emai@gmail.com", "gresit");
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email sau parola invalida"));
    }


    @Test
    void registerValidationErrorsAreReturned() throws Exception {
        UserCreateRequest invalidRequest = new UserCreateRequest(
                "",
                "",
                "not-an-email"
        );

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void loginReturnsUnauthorized() throws Exception {
        AuthLoginRequest loginRequest= new AuthLoginRequest("", "");
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }



    private String loginAndGetToken(String email, String password) throws Exception {
        MvcResult result=mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AuthLoginRequest(email, password))))
                .andExpect(status().isAccepted())
                .andReturn();

        AuthResponse authResponse=objectMapper.readValue(result.getResponse().getContentAsByteArray(), AuthResponse.class);
        return authResponse.token();
    }

    private CarResponse createCar(String brand, String model, int year, double price) throws Exception {
        CarCreateRequest request=new CarCreateRequest(brand, model, year, price);
        MvcResult result=mockMvc.perform(post("/api/v1/cars/add").contentType(MediaType.APPLICATION_JSON)
                .with(jwt().authorities(()->"car:write"))
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsByteArray(), CarResponse.class);
    }

    @Test
    void loginTokenCanAccessProtectedEndpoint() throws Exception {
        createCar("brand", "model", 1999, 22220.0);
        UserCreateRequest registeredUser=new UserCreateRequest("Nume", "email@gmail.com", "password");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registeredUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());

        String token=loginAndGetToken(registeredUser.email(), registeredUser.password());
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));

        mockMvc.perform(get("/api/v1/cars/all")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.carResponseList.length()").value(1));
    }
}
