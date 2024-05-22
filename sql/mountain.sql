-- 시퀀스 생성
CREATE SEQUENCE MEMBER_ID_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- 테이블 생성
CREATE TABLE MEMBER (
    MEMBER_ID NUMBER(8) DEFAULT MEMBER_ID_SEQ.NEXTVAL,
    INTERNAL_ID NUMBER(8),
    ID VARCHAR2(40) UNIQUE,
    PW VARCHAR2(10),
    TEL VARCHAR2(13),
    NICKNAME VARCHAR2(30),
    GENDER VARCHAR2(6) CHECK (GENDER IN ('남자', '여자')),
    MEXP NUMBER(1) CHECK (MEXP IN (0, 1, 2)),
    LOC VARCHAR2(10),
    GUBUN VARCHAR2(1) DEFAULT 'U' CHECK (GUBUN IN ('U', 'M')),
    PIC BLOB,
    CDATE TIMESTAMP DEFAULT systimestamp,
    UDATE TIMESTAMP DEFAULT systimestamp,
    CONSTRAINT MEMBER_ID_PK PRIMARY KEY (MEMBER_ID)
);



-- 시퀀스 생성
CREATE SEQUENCE BBS_ID_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- 테이블 생성
CREATE TABLE BBS (
    BBS_ID NUMBER(10) DEFAULT BBS_ID_SEQ.NEXTVAL,
    MNTN_CODE NUMBER(10),
    TITLE VARCHAR2(150),
    MEMBER_ID NUMBER(8),
    HIT NUMBER(5) DEFAULT 0,
    BCONTENT CLOB,
    STARING NUMBER(1) CHECK (STARING BETWEEN 1 AND 5),
    CTIME NUMBER(4),
    BPIC BLOB,
    STATUS VARCHAR2(1) DEFAULT 'N' CHECK (STATUS IN ('N', 'D', 'W')),
    CDATE TIMESTAMP DEFAULT systimestamp,
    UDATE TIMESTAMP DEFAULT systimestamp,
    CONSTRAINT BBS_ID_PK PRIMARY KEY (BBS_ID),
    CONSTRAINT BBS_MNTN_CODE_FK FOREIGN KEY (MNTN_CODE) REFERENCES mountain(MNTN_CODE),
    CONSTRAINT BBS_MEMBER_ID_FK FOREIGN KEY (MEMBER_ID) REFERENCES member(MEMBER_ID)
);


-- 시퀀스 생성
CREATE SEQUENCE RBBS_ID_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE TABLE RBBS (
    RBBS_ID NUMBER(10) DEFAULT RBBS_ID_SEQ.NEXTVAL,
    BBS_ID NUMBER(10),
    MEMBER_ID NUMBER(8),
    COMMENTS CLOB,
    CDATE TIMESTAMP DEFAULT systimestamp,
    UDATE TIMESTAMP DEFAULT systimestamp,
    CONSTRAINT RBBS_ID_PK PRIMARY KEY (RBBS_ID),
    CONSTRAINT RBBS_BBS_ID_FK FOREIGN KEY (BBS_ID) REFERENCES BBS(BBS_ID),
    CONSTRAINT RBBS_MEMBER_ID_FK FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER(MEMBER_ID)
);



-- 시퀀스 생성
CREATE SEQUENCE CLUB_ID_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- 테이블 생성
CREATE TABLE CLUB (
    CLUB_ID NUMBER(10) DEFAULT CLUB_ID_SEQ.NEXTVAL,
    TITLE VARCHAR2(150),
    MEMBER_ID NUMBER(8),
    HIT NUMBER(5) DEFAULT 0,
    CCONTENT CLOB,
    REGION VARCHAR2(10),
    CPIC BLOB,
    STATUS VARCHAR2(1) DEFAULT 'N' CHECK (STATUS IN ('N', 'D', 'W')),
    TIMETABLE TIMESTAMP,
    RECRUIT NUMBER(2),
    CDATE TIMESTAMP DEFAULT systimestamp,
    UDATE TIMESTAMP DEFAULT systimestamp,
    CONSTRAINT CLUB_ID_PK PRIMARY KEY (CLUB_ID),
    CONSTRAINT CLUB_MEMBER_ID_FK FOREIGN KEY (MEMBER_ID) REFERENCES member(MEMBER_ID)
);


-- 시퀀스 생성
CREATE SEQUENCE RCLUB_ID_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- 테이블 생성
CREATE TABLE RCLUB (
    RCLUB_ID NUMBER(10) DEFAULT RCLUB_ID_SEQ.NEXTVAL,
    CLUB_ID NUMBER(10),
    MEMBER_ID NUMBER(8),
    COMMENTS CLOB,
    CDATE TIMESTAMP DEFAULT systimestamp,
    UDATE TIMESTAMP DEFAULT systimestamp,
    CONSTRAINT RCLUB_ID_PK PRIMARY KEY (RCLUB_ID),
    CONSTRAINT RCLUB_CLUB_ID_FK FOREIGN KEY (CLUB_ID) REFERENCES CLUB(CLUB_ID),
    CONSTRAINT RCLUB_MEMBER_ID_FK FOREIGN KEY (MEMBER_ID) REFERENCES member(MEMBER_ID)
);



-- 시퀀스 생성
CREATE SEQUENCE MANAGER_INQUIRY_ID_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

-- 테이블 생성
CREATE TABLE MANAGER (
    INQUIRY_ID NUMBER(10) DEFAULT MANAGER_INQUIRY_ID_SEQ.NEXTVAL,
    MEMBER_ID NUMBER(8),
    INQUIRY_TITLE VARCHAR2(150),
    INQUIRY_CONTENT CLOB,
    INQUIRY_COMMENT CLOB,
    INQUIRY_STATE VARCHAR2(1),
    CONSTRAINT MANAGER_INQUIRY_ID_PK PRIMARY KEY (INQUIRY_ID),
    CONSTRAINT MANAGER_MEMBER_ID_FK FOREIGN KEY (MEMBER_ID) REFERENCES member(MEMBER_ID)
);


-- 기존 열 삭제
--ALTER TABLE bbs DROP COLUMN bpic;
--ALTER TABLE club DROP COLUMN cpic;
--ALTER TABLE member DROP COLUMN pic;
--
---- 새로운 열 추가
--ALTER TABLE bbs ADD (code VARCHAR2(10));
--ALTER TABLE club ADD (code VARCHAR2(10));
--ALTER TABLE member ADD (code VARCHAR2(10));


--테이블 삭제
drop table uploadfile;
drop table code;

--시퀀스 삭제
drop sequence uploadfile_uploadfile_id_seq;
drop sequence code_code_id_seq;


--코드
create table code(
    code_id     varchar2(11),       --코드
    decode      varchar2(30),       --코드명
    discript    clob,               --코드설명
    useyn       char(1) default 'Y',            --사용여부 (사용:'Y',미사용:'N')
    cdate       timestamp default systimestamp,         --생성일시
    udate       timestamp default systimestamp          --수정일시
);
--기본키
alter table code add Constraint code_code_id_pk primary key (code_id);

--제약조건
alter table code modify decode constraint code_decode_nn not null;
alter table code modify useyn constraint code_useyn_nn not null;
alter table code add constraint code_useyn_ck check(useyn in ('Y','N'));

--시퀀스
create sequence code_code_id_seq;


--첨부파일
create table uploadfile(
    uploadfile_id   number(10),     --파일아이디
    code            varchar2(11),   --분류코드
    rid             varchar2(10),     --참조번호(게시글번호등)
    store_filename  varchar2(100),   --서버보관파일명
    upload_filename varchar2(100),   --업로드파일명(유저가 업로드한파일명)
    fsize           varchar2(45),   --업로드파일크기(단위byte)
    ftype           varchar2(100),   --파일유형(mimetype)
    cdate           timestamp default systimestamp, --등록일시
    udate           timestamp default systimestamp  --수정일시
);
--기본키
alter table uploadfile add constraint uploadfile_uploadfile_id_pk primary key(uploadfile_id);

--제약조건
alter table uploadfile modify code constraint uploadfile_code_nn not null;
alter table uploadfile modify rid constraint uploadfile_rid_nn not null;
alter table uploadfile modify store_filename constraint uploadfile_store_filename_nn not null;
alter table uploadfile modify upload_filename constraint uploadfile_upload_filename_nn not null;
alter table uploadfile modify fsize constraint uploadfile_fsize_nn not null;
alter table uploadfile modify ftype constraint uploadfile_ftype_nn not null;

--시퀀스
create sequence uploadfile_uploadfile_id_seq;

ALTER TABLE bbs MODIFY code VARCHAR2(10) DEFAULT 'B';
ALTER TABLE club MODIFY code VARCHAR2(10) DEFAULT 'C';
ALTER TABLE member MODIFY code VARCHAR2(10) DEFAULT 'P';

commit;
