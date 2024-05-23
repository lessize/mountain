package com.kh.mountain.member.dao;

import com.kh.mountain.domain.entity.Member;
import com.kh.mountain.domain.member.dao.MemberDAO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
class MemberDAOImplTest {

  @Autowired
  MemberDAO memberDAO;

  @Test
  @DisplayName("회원가입")
  @Transactional
  void insertMember() {
    Member member = new Member();
    member.setId("user12@kh.com");
    member.setPw("1234");
    member.setNickname("사용자12");
    member.setTel("010-3456-7890");
    member.setGender("여자");
    member.setMexp(2);
    member.setLoc("울산광역시");

    Long memberId = memberDAO.insertMember(member);
    log.info("memberId={}", memberId);
  }
}