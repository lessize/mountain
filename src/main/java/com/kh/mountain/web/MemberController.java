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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    return memberId != null ? "redirect:/members/login" : "redirect:/";
  }

  // 로그인 페이지
  @GetMapping("/login")
  public String loginForm(Model model) {
    model.addAttribute("loginForm", new LoginForm());
    return "member/login";
  }

  // 로그인
  @PostMapping("/login")
  public String login(LoginForm loginForm, HttpServletRequest request,
                      @RequestParam(value = "redirectUrl",required = false) String redirectUrl){

    log.info("loginForm={}", loginForm);

    if (memberSVC.existId(loginForm.getId())) {
      Optional<Member> optionalMember = memberSVC.findByEmailPw(loginForm.getId(), loginForm.getPw());

      if (optionalMember.isPresent()) {
        HttpSession session = request.getSession(true);
        Member member = optionalMember.get();

        LoginMember loginMember = new LoginMember(
                member.getMemberId(),
                member.getId(),
                member.getPw(),
                member.getTel(),
                member.getNickname(),
                member.getGender(),
                member.getMexp(),
                member.getLoc(),
                member.getGubun(),
                member.getCode()
        );
        session.setAttribute("loginMember", loginMember);
        log.info("loginMember={}", loginMember);

        if (redirectUrl != null && !redirectUrl.isEmpty()) {
          return "redirect:" + redirectUrl;
        }
        return "redirect:/index"; // 로그인 성공 시 인덱스 페이지로 이동
      } else {
        log.info("Invalid password for id={}", loginForm.getId());
        return "redirect:/members/login?error=true"; // 로그인 실패 시 로그인 페이지로 이동
      }
    } else {
      log.info("Id not found: {}", loginForm.getId());
      return "redirect:/members/login?error=true"; // 로그인 실패 시 로그인 페이지로 이동
    }
  }

  @ResponseBody
  @PostMapping("/logout")
  public String logout(HttpServletRequest request) {
    String flag = "NOK";

    HttpSession session = request.getSession(false);

    if (session != null) {
      session.invalidate();
      flag = "OK";
    }
    return flag;
  }

  // 프로필 조회
  @GetMapping("/profile")
  public String profile(@RequestParam("id") String id, Model model) {
    Optional<Member> memberOptional = memberSVC.findById(id);
    if (memberOptional.isPresent()) {
      Member member = memberOptional.get();
      model.addAttribute("member", member);
      return "member/profile";
    } else {
      // 멤버가 존재하지 않는 경우 처리
      return "error"; // 예를 들어, 오류 페이지로 리다이렉트 또는 오류 메시지 표시
    }
  }
}
