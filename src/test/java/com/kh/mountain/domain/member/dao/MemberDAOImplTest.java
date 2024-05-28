package com.kh.mountain.domain.member.dao;

import com.kh.mountain.domain.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
class MemberDAOImplTest {

  @Autowired
  MemberDAO memberDAO;

  @Test
  @DisplayName("이메일 있음")
  void existEmail() {
    boolean exist = memberDAO.existId("user1@kh.com");
    Assertions.assertThat(exist).isEqualTo(true);
  }

  @Test
  @DisplayName("이메일 없음")
  void notExistEmail() {
    boolean exist = memberDAO.existId("15420@kh.com");
    Assertions.assertThat(exist).isEqualTo(false);
  }

  @Test
  @DisplayName("회원 있음")
  void findByEmailPw() {
    Optional<Member> optionalMember = memberDAO.findByEmailPw("user1@kh.com", "1234");

    Assertions.assertThat(optionalMember).isPresent();
    Member member = optionalMember.get();
    Assertions.assertThat(member.getId()).isEqualTo("user1@kh.com");
    Assertions.assertThat(member.getPw()).isEqualTo("1234");
  }

  @Test
  @DisplayName("회원 없음")
  void notfindByEmailPw() {
    Optional<Member> optionalMember = memberDAO.findByEmailPw("323434@kh.com", "4231");

    Assertions.assertThat(optionalMember).isEmpty();
  }

//  @Test
//  @DisplayName("프로필 조회")
//  void findById() {
//    String id = "user1@kh.com";
//    Optional<Member> findMember = memberDAO.findById(id);
//    Member member = findMember.orElse(null);
//    log.info("member={}", member);
//  }

  @Test
  @DisplayName("프로필 수정")
  void updateById() {
    String id = "user1@kh.com";
    Member member = new Member();
    member.setId(id);

    member.setTel("010-1111-2222");
    member.setNickname("사용자1");
    member.setMexp(0);
    member.setLoc("서울특별시");

    int updatedRowCnt = memberDAO.updateById(id, member);

    if (updatedRowCnt == 0) {
      Assertions.fail("변경할 내역이 없습니다.");
    }

//    Optional<Member> optionalMember = memberDAO.findById(id);
//    if (optionalMember.isPresent()) {
//      Member foundMember = optionalMember.get();
//      Assertions.assertThat(foundMember.getTel()).isEqualTo("010-1111-2222");
//      Assertions.assertThat(foundMember.getNickname()).isEqualTo("사용자1");
//      Assertions.assertThat(foundMember.getMexp()).isEqualTo(0);
//      Assertions.assertThat(foundMember.getLoc()).isEqualTo("서울특별시");
//    } else {
//      Assertions.fail("변경된 회원을 찾을 수 없습니다.");
//    }
  }

  @Test
  @DisplayName("아이디찾기")
  void findByNicknameTel() {
    // 테스트할 닉네임과 전화번호 설정
    String nickname = "사용자1";
    String tel = "010-1111-2222";
    String id = "user1@kh.com";

    // DAO 메서드 호출
    Optional<Member> optionalMember = memberDAO.findByNicknameTel(nickname, tel);

    // 회원 객체 가져오기
    Member member = optionalMember.orElseThrow(() -> new AssertionError("회원을 찾지 못했습니다."));

    // AssertJ를 사용한 검증
    Assertions.assertThat(member.getId()).isEqualTo(id);
  }

  @Test
  @DisplayName("비밀번호 유무")
  void hasPasswd() {
    // 테스트할 아이디와 닉네임 설정
    String id = "user1@kh.com";
    String nickname = "사용자1";

    // hasPasswd 메서드 호출
    boolean hasPassword = memberDAO.hasPasswd(id, nickname);

    // 비밀번호 존재 여부 검증
    Assertions.assertThat(hasPassword).isTrue(); // 예상대로 비밀번호가 존재해야 함
  }

  @Test
  @DisplayName("비밀번호 변경")
  void changePw() {
    // 테스트할 아이디와 변경할 비밀번호 설정
    String id = "user1@kh.com";
    String newPassword = "1234";

    // changePw 메서드 호출
    int updateCount = memberDAO.changePw(id, newPassword);

    // 업데이트된 행 수 검증
    Assertions.assertThat(updateCount).isEqualTo(1); // 업데이트는 정확히 한 건이어야 함
  }
}