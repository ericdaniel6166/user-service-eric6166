package com.eric6166.user.dto;

import com.eric6166.base.utils.BaseConst;
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
public class GetTokenRequest {

    @NotBlank
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    String username;

    @NotBlank
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    @ToString.Exclude
    String password;

}
