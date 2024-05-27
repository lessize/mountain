package com.kh.mountain.domain.uploadFile.dao; // 패키지 문장 추가

import com.kh.mountain.web.form.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UploadFileDAOImpl implements UploadFileDAO {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final String uploadDir = "src\\main\\resources\\static\\profile-images";

  @Autowired
  private final HttpServletRequest request;

  @Value("${profile.image.defaultCode}")
  private String profileImageDefaultCode;

  // 프로필 사진 업로드
  @Transactional
  public String saveProfileImage(MultipartFile file) throws IOException {
    // 파일 저장 디렉토리 경로 설정
    Path uploadPath = Paths.get(uploadDir);

//    log.info(uploadPath);

    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    // 세션에서 로그인한 회원 정보 가져오기
    LoginMember loginMember = (LoginMember) request.getSession().getAttribute("loginMember");
    String id = loginMember.getId();

    // 기존에 저장된 프로필 사진 삭제 (단일 파일 관리를 위한 방법)
    deleteExistingProfileImage(id);

    // 파일명 생성 (UUID 활용)
    String originalFilename = file.getOriginalFilename();
    String storeFilename = UUID.randomUUID().toString() + "_" + originalFilename;
    Path filePath = uploadPath.resolve(storeFilename);

    // 파일 크기 읽어오기
    long fileSize = file.getSize(); // 파일 크기(bytes)

    // 파일 유형(MIME 타입) 읽어오기
    String fileType = file.getContentType(); // 파일 유형(MIME 타입)

    // 파일 저장
    Files.copy(file.getInputStream(), filePath);

    // 데이터베이스에 업로드 정보 저장
    String sql = "INSERT INTO UploadFile (UPLOADFILE_ID, RID, UPLOAD_FILENAME, STORE_FILENAME, FSIZE, FTYPE, CODE) " +
            "VALUES (UPLOADFILE_UPLOADFILE_ID_SEQ.nextval, :rid, :uploadFilename, :storeFilename, :fileSize, :fileType, :code)";
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("rid", id); // 참조 번호
    params.addValue("uploadFilename", originalFilename); // 업로드 파일명
    params.addValue("storeFilename", storeFilename); // 서버 보관 파일명
    params.addValue("fileSize", fileSize); // 업로드 파일 크기
    params.addValue("fileType", fileType); // 파일 유형
    params.addValue("code", profileImageDefaultCode); // 코드

    jdbcTemplate.update(sql, params);

    return storeFilename;
  }

  public void deleteExistingProfileImage(String id) {
    // 기존에 저장된 프로필 사진을 삭제하는 로직 추가
  }
}
