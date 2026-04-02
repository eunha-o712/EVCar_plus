package com.evcar.repository.user;

import com.evcar.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 로그인
    Optional<User> findByLoginId(String loginId);

    // 아이디 찾기
    Optional<User> findByNameAndEmail(String name, String email);

    // 비밀번호 찾기
    Optional<User> findByLoginIdAndNameAndEmail(String loginId, String name, String email);

    // 회원가입 중복 체크
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
}