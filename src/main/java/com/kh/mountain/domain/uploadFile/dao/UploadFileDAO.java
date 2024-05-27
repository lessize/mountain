package com.kh.mountain.domain.uploadFile.dao;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileDAO {

  public String saveProfileImage(MultipartFile file) throws IOException;

  void deleteExistingProfileImage(String id);
}
