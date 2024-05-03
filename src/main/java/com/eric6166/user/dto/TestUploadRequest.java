package com.eric6166.user.dto;

import com.eric6166.base.dto.PasswordDto;
import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.validation.PasswordMatches;
import com.eric6166.base.validation.ValidFileExtension;
import com.eric6166.base.validation.ValidFileMaxSize;
import com.eric6166.base.validation.ValidFileMimeType;
import com.eric6166.base.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordMatches
public class TestUploadRequest implements PasswordDto {

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
    String password;

    @NotBlank
    @Size(max = BaseConst.DEFAULT_SIZE_MAX_STRING)
    @ToString.Exclude
    String confirmPassword;

    @NotNull
    @ValidFileMaxSize(maxSize = 10)
    @ValidFileExtension(extensions = {"pdf"})
    @ValidFileMimeType(mimeTypes = {"application/pdf"})
    MultipartFile file;

}
