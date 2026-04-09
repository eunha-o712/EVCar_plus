package com.evcar.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 비밀번호 재설정 DTO
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetDto {

    private String loginId;

    private String name;

    private String email;

    private String newPassword;

    private String newPasswordConfirm;

}