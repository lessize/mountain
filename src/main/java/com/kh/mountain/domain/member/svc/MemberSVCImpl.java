package com.kh.mountain.domain.member.svc;

import com.kh.mountain.domain.member.dao.MemberDAO;
import com.kh.mountain.domain.entity.Member;
import com.kh.mountain.web.form.MemberProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSVCImpl implements MemberSVC{

  private final MemberDAO memberDAO;

  // 회원가입
  @Override
  public Long insertMember(Member member) {
    return memberDAO.insertMember(member);
  }

  // 이메일 유무 조회
  public boolean existId(String id) {
    return memberDAO.existId(id);
  }

  // 회원조회
  @Override
  public Optional<Member> findByEmailPw(String id, String pw) {
    return memberDAO.findByEmailPw(id, pw);
  }

  // 프로필 조회
  @Override
  public Optional<MemberProfile> findById(String id) {
    return memberDAO.findById(id);
  }
}
