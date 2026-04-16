package com.evcar.repository.user;

import com.evcar.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);

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
}