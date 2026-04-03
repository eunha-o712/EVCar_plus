<<<<<<< HEAD
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
=======
package com.evcar.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 아이디 찾기 DTO
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdRecoveryDto {

    private String name;

    private String emailId;
    private String emailDomain;
}
>>>>>>> ccef92c2bb418a9ae53ae1d629a62927994a692e
