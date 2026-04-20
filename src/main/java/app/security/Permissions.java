package app.security;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Permissions {
    CAR_READ("Car:Read"),
    CAR_WRITE("Car:Write"),
    USER_READ("User:Read"),
    USER_WRITE("User:Write"),
    READ_USER_PERMISSION("UserPermission:Read");

    private final String permission;
    public String getPermission(){
        return permission;
    };
}
