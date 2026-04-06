package com.evcar.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminConsultReplyRequestDto {

    private String consultStatus;
    private String consultResult;
    private String adminReply;
}