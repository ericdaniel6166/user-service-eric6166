package com.eric6166.user.dto;

import com.eric6166.base.utils.BaseConst;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
public class GetTokenRequest {

    @NotBlank
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    private String username;

    @NotBlank
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    @ToString.Exclude
    private String password;

}
