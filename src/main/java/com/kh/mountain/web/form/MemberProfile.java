package com.kh.mountain.web.form;

import com.kh.mountain.domain.entity.Member;
import lombok.Data;

@Data
public class MemberProfile {
  private Member member;
  private String profileImageUrl;
}
