package com.eric6166.user.dto;

import com.eric6166.common.dto.AccountDto;
import com.eric6166.common.utils.Const;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
//@PasswordMatches
//public class RegisterAccountRequest implements AccountDto, PasswordDto {
public class RegisterAccountRequest implements AccountDto {
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
    @ToString.Exclude
    String password;

    @NotBlank
    @Size(max = Const.DEFAULT_SIZE_MAX_STRING)
    @ToString.Exclude
    String confirmPassword;

}
