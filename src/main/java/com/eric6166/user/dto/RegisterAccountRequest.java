package com.eric6166.user.dto;

import com.eric6166.base.dto.PasswordDto;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.validation.PasswordMatches;
import com.eric6166.base.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@PasswordMatches
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAccountRequest implements PasswordDto {

    @NotBlank
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    String username;

    @NotBlank
    @Email
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    String email;

    @NotBlank
    @ValidPassword
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    @ToString.Exclude
    String password; // improvement: encryption

    @NotBlank
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    @ToString.Exclude
    String confirmPassword;

}
