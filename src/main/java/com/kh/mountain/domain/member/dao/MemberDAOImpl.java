package com.kh.mountain.domain.member.dao;

import com.kh.mountain.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDAOImpl implements MemberDAO {

  private final NamedParameterJdbcTemplate template;

  // 회원 가입
  @Override
  public Long insertMember(Member member) {
    // sql
    StringBuffer sql = new StringBuffer();
    sql.append("insert into member (member_id, id, pw, tel, nickname, gender, mexp, loc) ");
    sql.append("values (member_member_id_seq.nextval, :id, :pw, :tel, :nickname, :gender, :mexp, :loc) ");

    SqlParameterSource param = new BeanPropertySqlParameterSource(member);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql.toString(), param, keyHolder, new String[]{"member_id"});
    Long member_id = ((BigDecimal) keyHolder.getKeys().get("member_id")).longValue();

    return member_id;
  }

  // 이메일 유무
  public boolean existId(String id) {
    // Null check for template
    if (template == null) {
      throw new IllegalStateException("JdbcTemplate is not initialized");
    }

    String sql = "select count(id) from member where id = :id ";
    Map<String, Object> paramMap = Collections.singletonMap("id", id);

    Integer count = template.queryForObject(sql, paramMap, Integer.class);

    // Check if count is exactly 1
    return count != null && count == 1;
  }

  // 회원 조회
  @Override
  public Optional<Member> findByEmailPw(String id, String pw) {
    StringBuffer sql = new StringBuffer();
    sql.append("select *" );
    sql.append("  from member" );
    sql.append(" where id = :id" );
    sql.append("   and pw = :pw" );

    Map param = Map.of("id", id, "pw", pw);
    try {
      Member member = template.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<>(Member.class));
      return Optional.of(member);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }
}
