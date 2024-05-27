package com.kh.mountain.domain.uploadFile.svc;

import com.kh.mountain.domain.uploadFile.dao.UploadFileDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadFileSVCImpl implements UploadFileSVC {

  private final UploadFileDAO uploadFileDAO;

  @Override
  public String saveProfileImage(MultipartFile file) throws IOException {
    return uploadFileDAO.saveProfileImage(file);
  }
}
