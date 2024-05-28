package com.kh.mountain.domain.member.svc;

import com.kh.mountain.domain.entity.Member;
import com.kh.mountain.web.form.MemberProfile;

import java.util.Optional;

public interface MemberSVC {
  // 회원 가입
  Long insertMember(Member member);

  // 이메일 유무 조회
  public boolean existId(String id);

  // 회원 조회
  Optional<Member> findByEmailPw(String id, String pw);

  // 프로필 조회
  Optional<MemberProfile> findById(String id);

  // 아이디 찾기
  Optional<Member> findByNicknameTel(String nickname, String tel);

  //비밀번호 유무확인
  boolean hasPasswd(String id, String nickname);

  //비밀번호 변경
  int changePw(String id, String pw);
}
