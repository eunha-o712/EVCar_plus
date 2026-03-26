package com.evcar.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 아이디 찾기 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdRecoveryDto {

    private String name;

    private String email;
}