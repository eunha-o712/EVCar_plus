package com.evcar.dto.inquiry;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryCreateRequestDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}