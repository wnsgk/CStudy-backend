package com.CStudy.domain.file.application;

import com.CStudy.global.util.LoginUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<String> uploadFiles(MultipartFile[] multipartFileList, LoginUserDto loginUserDto) throws Exception;

    byte[] getImageBytes(LoginUserDto loginUserDto);
}
