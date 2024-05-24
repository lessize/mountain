package com.kh.mountain.domain.member.dao;

import com.kh.mountain.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

  // 프로필 조회
  @Override
  public Optional<Member> findById(String id) {
    StringBuffer sql = new StringBuffer();
    sql.append("select id, pw, tel, nickname, gender, mexp, loc, code " );
    sql.append("  from member " );
    sql.append(" where id = :id " );

    try {
      Map<String, Object> map = Map.of("id", id);
      Member member = template.queryForObject(sql.toString(), map, BeanPropertyRowMapper.newInstance(Member.class));
      return Optional.of(member);
    }catch (EmptyResultDataAccessException e){
      // 조회결과가 없는 경우
      return Optional.empty();
    }
  }

  // 프로필 수정
  @Override
  public int updateById(String id, Member member) {
    StringBuffer sql = new StringBuffer();
    sql.append("update member ");
    sql.append("   set tel = :tel, ");
    sql.append("       nickname = :nickname, ");
    sql.append("       mexp = :mexp, ");
    sql.append("       loc = :loc ");
    sql.append("where id = :id ");

    SqlParameterSource param = new MapSqlParameterSource()
            .addValue("id", id)
            .addValue("tel", member.getTel())
            .addValue("nickname", member.getNickname())
            .addValue("mexp", member.getMexp())
            .addValue("loc", member.getLoc());
    int updateProfile = template.update(sql.toString(), param);

    return updateProfile;
  }
}
