package com.kh.mountain.web;

import com.kh.mountain.domain.common.mail.MailService;
import com.kh.mountain.domain.entity.Member;
import com.kh.mountain.domain.member.svc.MemberSVC;
import com.kh.mountain.web.form.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberSVC memberSVC;
  private final MailService mailService;

  // 회원가입 페이지
  @GetMapping("/join")
  public String joinForm() {
    return "/member/joinForm";
  }

  // 회원 가입하기
  @PostMapping("/join")
  public String join(@RequestBody JoinForm joinForm) {
    log.info("joinForm = {}", joinForm);

    // 가입
    Member member = new Member();
    BeanUtils.copyProperties(joinForm, member);
    Long memberId = memberSVC.insertMember(member);

    log.info("member = {}", member);

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
                      @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {

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

        // 로그인 성공 시 리다이렉트
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
          return "redirect:" + redirectUrl;
        } else {
          return "redirect:/"; // 리다이렉트로 변경
        }
      }
    }

    // 로그인 실패 시 로그인 페이지로 리다이렉트
    return "redirect:/login"; // 리다이렉트로 변경
  }


  // 로그아웃
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
  public String viewProfile(HttpServletRequest request, Model model) {
    LoginMember loginMember = (LoginMember) request.getSession().getAttribute("loginMember");
    if (loginMember != null) {
      Optional<MemberProfile> memberProfileOpt = memberSVC.findById(loginMember.getId());
      if (memberProfileOpt.isPresent()) {
        MemberProfile memberProfile = memberProfileOpt.get();
        model.addAttribute("memberProfile", memberProfile);
      } else {
        // 프로필이 없는 경우 처리
        model.addAttribute("memberProfile", null);
      }
    } else {
      // 로그인되지 않은 사용자 처리
      return "redirect:/members/login"; // 로그인 페이지로 리다이렉트
    }

    return "member/profile";
  }

  // 아이디 찾기 양식
  @GetMapping("/findId")
  public String findIdForm() {
    return "member/findIdForm";
  }

  // 아이디 찾기
  @ResponseBody
  @PostMapping("findId")
  public ResponseEntity<Map<String, String>> findMyId(@RequestBody IdForm idForm) {
    Optional<Member> member = memberSVC.findByNicknameTel(idForm.getNickname(), idForm.getTel());
    Map<String, String> response = new HashMap<>();
    if (member.isPresent()) {
      response.put("status", "success");
      response.put("id", member.get().getId());
    } else {
      response.put("status", "error");
      response.put("message", "존재하지 않는 회원입니다.");
    }
    return ResponseEntity.ok(response);
  }

  //비밀번호 찾기 양식
  @GetMapping("/findPwd")
  public String findPwdForm(){

    return "member/findPwForm";
  }

  //비밀번호 찾기
  @ResponseBody
  @PostMapping("/findPwd")
  public String findPwd(@RequestBody PwForm pwForm){

    log.info("pwForm={}", pwForm);

    boolean hasPasswd = memberSVC.hasPasswd(pwForm.getId(), pwForm.getNickname());
    if (hasPasswd) {
      // 임시번호 생성 6자리 랜덤 생성
      String tmpPwd = UUID.randomUUID().toString().substring(0,6);
      // 2)회원 비밀번호를 임시 비밀번호로 변경
      memberSVC.changePw(pwForm.getId(), tmpPwd);
      // 메일 발송
      StringBuffer str = new StringBuffer();
      str.append("<html>");
      str.append("<p><b>");
      str.append(tmpPwd);
      str.append("</b></p>");
      str.append("<p><a href='http://localhost:9080/members/login'>로그인 후 비밀번호를 변경하시기 바랍니다.</a></p>");
      str.append("</html>");

      mailService.sendMail(pwForm.getId(),"임시비밀번호 송부",str.toString());
      return "redirect:/";
    }

    return "회원정보가 없습니다.";
  }
}
