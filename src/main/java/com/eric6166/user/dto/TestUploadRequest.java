package com.eric6166.user.dto;

import com.eric6166.base.utils.BaseConst;
import com.eric6166.base.validation.ValidFileExtension;
import com.eric6166.base.validation.ValidFileMaxSize;
import com.eric6166.base.validation.ValidFileMimeType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestUploadRequest {

    @NotNull
    @ValidFileMaxSize(maxSize = 10)
    @ValidFileExtension(extensions = {BaseConst.EXTENSION_PDF})
    @ValidFileMimeType(mimeTypes = {BaseConst.MIME_TYPE_PDF})
    MultipartFile pdfFile;

}
