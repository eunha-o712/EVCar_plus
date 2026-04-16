package com.evcar.dto.consultation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ConsultationCreateRequestDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String vehicleId;

    @NotBlank
    private String preferredDatetime;

    @NotNull
    @Min(0)
    private Integer budget;

    @NotBlank
    private String purchasePlan;

    @NotBlank
    private String consultContent;
}