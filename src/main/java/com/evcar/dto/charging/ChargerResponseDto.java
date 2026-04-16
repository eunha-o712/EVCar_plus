package com.evcar.dto.charging;

import com.evcar.domain.charging.Charger;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChargerResponseDto {

    private String chargerType;
    private String status;

    public static ChargerResponseDto from(Charger charger) {
        return new ChargerResponseDto(
                convertType(charger.getChargerType()),
                convertStatus(charger.getStatus())
        );
    }

    private static String convertType(String type) {
        return switch (type) {
            case "01" -> "DC차데모";
            case "02" -> "AC완속";
            case "03" -> "DC차데모+AC3상";
            case "04" -> "DC콤보";
            case "05" -> "DC차데모+DC콤보";
            case "06" -> "DC차데모+AC3상+DC콤보";
            case "07" -> "AC3상";
            case "08" -> "DC콤보(완속)";
            case "09" -> "NACS";
            case "10" -> "DC콤보+NACS";
            case "11" -> "DC콤보2(버스전용)";
            default -> "기타";
        };
    }

    private static String convertStatus(String status) {
        return switch (status) {
            case "0", "00" -> "알수없음";
            case "1", "01" -> "통신이상";
            case "2", "02" -> "사용가능";
            case "3", "03" -> "충전중";
            case "4", "04" -> "운영중지";
            case "5", "05" -> "점검중";
            default -> "알수없음";
        };
    }
}