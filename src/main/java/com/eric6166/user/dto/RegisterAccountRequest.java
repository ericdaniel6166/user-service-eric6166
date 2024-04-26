package com.eric6166.user.dto;

import com.eric6166.common.utils.Const;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
//@PasswordMatches
//public class RegisterAccountRequest implements AccountDto, PasswordDto {
public class RegisterAccountRequest {
    @NotBlank
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    String username;

    @NotBlank
    @Email
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    String email;

    @NotBlank
//    @ValidPassword
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    String password;

    @NotBlank
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    String confirmPassword;
}
