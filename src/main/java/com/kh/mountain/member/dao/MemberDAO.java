package com.kh.mountain.member.dao;

import com.kh.mountain.entity.Member;

public interface MemberDAO {
  // 회원 가입
  Member insertMember(Member member);
}
