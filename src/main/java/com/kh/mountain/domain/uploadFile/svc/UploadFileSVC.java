package com.kh.mountain.domain.uploadFile.svc;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileSVC {
  public String saveProfileImage(MultipartFile file) throws IOException;
}
