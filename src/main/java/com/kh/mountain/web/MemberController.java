package com.kh.mountain.web;

import com.kh.mountain.domain.entity.Member;
import com.kh.mountain.domain.member.svc.MemberSVC;
import com.kh.mountain.web.form.JoinForm;
import com.kh.mountain.web.form.LoginForm;
import com.kh.mountain.web.form.LoginMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberSVC memberSVC;

  // 회원가입 페이지
  @GetMapping("/join")
  public String joinForm() {
    return "/member/joinForm";
  }

  // 회원 가입하기
  @PostMapping("/join")
  public String join(JoinForm joinForm) {
    log.info("joinForm = {}", joinForm);

    // 가입
    Member member = new Member();
    BeanUtils.copyProperties(joinForm, member);
    Long memberId = memberSVC.insertMember(member);

    return "";
  }

  // 로그인 페이지
  @GetMapping("/login")
  public String loginForm() {
    return "member/login";
  }

  // 로그인
  @PostMapping("/login")
  public String login(LoginForm loginForm, HttpServletRequest request,
                      @RequestParam(value = "redirectUrl",required = false) String redirectUrl){

    if (memberSVC.existId(loginForm.getId())) {

      Optional<Member> optionalMember = memberSVC.findByEmailPw(loginForm.getId(), loginForm.getPw());
      //3) 회원인경우 회원 정보를 세션에 저장
      if (optionalMember.isPresent()) {
        //세션 생성  : 세션정보가 있으면 있는 세션정보를 없으면 새로이 생성 getSession(true)
        HttpSession session = request.getSession(true);
        //회원 정보를 세션에 저장
        Member member = optionalMember.get();
        LoginMember loginMember = new LoginMember(
//                member.getMemberId(),
//                member.getId(),
//                member.getGubun()
        );
        session.setAttribute("loginMember", loginMember);
      }
    }
    return "";
  }
}
