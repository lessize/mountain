package com.kh.mountain.domain.entity;

import java.time.LocalDateTime;

public class Code {
  // 코드 아이디
  private String CODE_ID;            // 	VARCHAR2(11 BYTE)
  // 코드명
  private String DECODE;             // 	VARCHAR2(30 BYTE)
  // 코드 설명
  private String DISCRIPT;           // 	CLOB
  // 사용 여부
  private String USEYN;              // 	CHAR(1 BYTE)
  // 생성 일시
  private LocalDateTime CDATE;       // 	TIMESTAMP(6)
  // 수정 일시
  private LocalDateTime UDATE;       // 	TIMESTAMP(6)
}
