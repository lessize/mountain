package com.kh.mountain.domain.member.svc;

import com.kh.mountain.domain.entity.Member;

import java.util.Optional;

public interface MemberSVC {
  // 회원 가입
  Long insertMember(Member member);

  // 이메일 유무 조회
  public boolean existId(String id);

  // 회원 조회
  Optional<Member> findByEmailPw(String id, String pw);

  // 프로필 조회
  Optional<Member> findById(String id);
}
