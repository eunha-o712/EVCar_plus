package com.evcar.repository.user;

import com.evcar.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByNameAndEmail(String name, String email);
    Optional<User> findByLoginIdAndNameAndEmail(String loginId, String name, String email);
}