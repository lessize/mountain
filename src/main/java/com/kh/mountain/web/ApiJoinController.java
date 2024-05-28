package com.kh.mountain.web;

import com.kh.mountain.domain.member.svc.MemberSVC;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class ApiJoinController {

  private final MemberSVC memberSVC;

  // 이메일 중복 체크
  @GetMapping("/checkEmail")
  public ResponseEntity<String> checkEmail(@RequestParam String id) {
    boolean isExist = memberSVC.existId(id);
    if (isExist) {
      return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
    }
    return ResponseEntity.ok("사용 가능한 이메일입니다.");
  }

  // 회원 가입
//  @PostMapping("/join")
//  public ResponseEntity<Map<String, String>> join(@RequestBody JoinForm joinForm) {
//    Map<String, String> response = new HashMap<>();
//
//    // 이메일 중복 체크
//    if (memberSVC.existId(joinForm.getId())) {
//      response.put("message", "이미 사용 중인 이메일입니다.");
//      return ResponseEntity.badRequest().body(response);
//    }
//
//    // 회원 정보 저장 (임시)
//    Member member = new Member();
//    BeanUtils.copyProperties(joinForm, member);
//    member.setEnabled(false); // 이메일 인증 전까지 활성화되지 않음
//    Long memberId = memberSVC.insertMember(member);
//
//    // 이메일 인증 토큰 생성 및 전송
//    String token = UUID.randomUUID().toString();
//    memberSVC.createVerificationToken(member, token);
//    memberSVC.sendEmailVerification(member.getId(), token);
//
//    response.put("message", "이메일 인증을 위해 메일을 확인해주세요.");
//    return ResponseEntity.ok(response);
//  }

  // 이메일 인증
//  @GetMapping("/verify")
//  public ResponseEntity<String> verifyEmail(@RequestParam String token) {
//    Optional<Member> member = memberSVC.verifyEmailToken(token);
//    if (member.isPresent()) {
//      return ResponseEntity.ok("이메일 인증이 완료되었습니다. 로그인해 주세요.");
//    } else {
//      return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
//    }
//  }
}
