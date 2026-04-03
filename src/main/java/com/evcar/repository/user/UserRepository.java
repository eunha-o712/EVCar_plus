<<<<<<< HEAD
package com.evcar.repository.user;

import com.evcar.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.loginId = :loginId")
    boolean existsByLoginId(@Param("loginId") String loginId);

    boolean existsByEmail(String email);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByNameAndEmail(String name, String email);

    Optional<User> findByLoginIdAndNameAndEmail(String loginId, String name, String email);

    @Query(
            value = """
                    SELECT user_id
                    FROM user
                    ORDER BY CAST(SUBSTRING(user_id, 5) AS UNSIGNED) DESC
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    Optional<String> findLastUserId();
=======
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
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
}