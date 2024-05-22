package com.kh.mountain.member.dao;

import com.kh.mountain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO {

  private final NamedParameterJdbcTemplate template;

  // 회원 가입
  @Override
  public Member insertMember(Member member) {
    // sql

    return null;
  }
}
