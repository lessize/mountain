package com.kh.mountain.web;

import com.kh.mountain.domain.uploadFile.svc.UploadFileSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UploadFileController {

  private final UploadFileSVC uploadFileSVC;

  @Value("${profile.image-url-prefix}")
  private String imageUrlPrefix;

  @PostMapping("/profile/upload")
  public String uploadProfileImage(@RequestParam("file") MultipartFile file,
                                   @RequestParam("id") String id,
                                   Model model) throws IOException {

    // 파일 업로드 및 저장
    String storeFilename = uploadFileSVC.saveProfileImage(file);

    // 프로필 사진 URL 설정
    String profileImageUrl = storeFilename;
    model.addAttribute("profileImageUrl", profileImageUrl);

    log.info(profileImageUrl);

    // 프로필 수정 페이지로 이동
    return "redirect:/member/profile";
  }
}
