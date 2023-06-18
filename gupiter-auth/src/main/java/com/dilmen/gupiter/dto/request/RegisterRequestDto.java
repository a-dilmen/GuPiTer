package com.dilmen.gupiter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequestDto {
    @NotBlank(message = "email cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "email must be valid please check your email")
    private  String email;
    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*!.,\\(\\)\\{\\[\\]\\}])(?=\\S+$).{8,64}$",
            message = "password must consist at least 1 Capital 1 Lowercase Letter 1 number and 1 special character (@#$%^&+=*!.,{[()]})and isze must be beetween 8-64")
    private String password;
    @NotBlank(message = "re-password cannot be empty")
    private String rePassword;
}
