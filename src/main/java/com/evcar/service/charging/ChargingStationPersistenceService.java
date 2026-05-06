package com.evcar.service.charging;

import com.evcar.domain.charging.Charger;
import com.evcar.domain.charging.ChargingStation;
import com.evcar.repository.charging.ChargerRepository;
import com.evcar.repository.charging.ChargingStationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChargingStationPersistenceService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final ChargingStationRepository chargingStationRepository;
    private final ChargerRepository chargerRepository;
    private final ChargingStationValidator chargingStationValidator;

    // ─────────────────────────────────────────────
    // 시도명 → zcode 매핑
    // ─────────────────────────────────────────────
    private static final Map<String, String> SIDO_ZCODE_MAP = Map.ofEntries(
            Map.entry("서울특별시",    "11"),
            Map.entry("부산광역시",    "26"),
            Map.entry("대구광역시",    "27"),
            Map.entry("인천광역시",    "28"),
            Map.entry("광주광역시",    "29"),
            Map.entry("대전광역시",    "30"),
            Map.entry("울산광역시",    "31"),
            Map.entry("세종특별자치시", "36"),
            Map.entry("경기도",       "41"),
            Map.entry("강원도",       "42"),
            Map.entry("강원특별자치도", "42"),
            Map.entry("충청북도",     "43"),
            Map.entry("충청남도",     "44"),
            Map.entry("전라북도",     "45"),
            Map.entry("전북특별자치도", "45"),
            Map.entry("전라남도",     "46"),
            Map.entry("경상북도",     "47"),
            Map.entry("경상남도",     "48"),
            Map.entry("제주특별자치도", "50")
    );

    // ─────────────────────────────────────────────
    // "zcode_시군구명" → zscode 매핑
    // ─────────────────────────────────────────────
    private static final Map<String, String> SIGUNGU_ZSCODE_MAP;

    static {
        Map<String, String> map = new LinkedHashMap<>();

        // 서울
        map.put("11_종로구",   "11110"); map.put("11_중구",     "11140");
        map.put("11_용산구",   "11170"); map.put("11_성동구",   "11200");
        map.put("11_광진구",   "11215"); map.put("11_동대문구", "11230");
        map.put("11_중랑구",   "11260"); map.put("11_성북구",   "11290");
        map.put("11_강북구",   "11305"); map.put("11_도봉구",   "11320");
        map.put("11_노원구",   "11350"); map.put("11_은평구",   "11380");
        map.put("11_서대문구", "11410"); map.put("11_마포구",   "11440");
        map.put("11_양천구",   "11470"); map.put("11_강서구",   "11500");
        map.put("11_구로구",   "11530"); map.put("11_금천구",   "11545");
        map.put("11_영등포구", "11560"); map.put("11_동작구",   "11590");
        map.put("11_관악구",   "11620"); map.put("11_서초구",   "11650");
        map.put("11_강남구",   "11680"); map.put("11_송파구",   "11710");
        map.put("11_강동구",   "11740");

        // 부산
        map.put("26_중구",    "26110"); map.put("26_서구",    "26140");
        map.put("26_동구",    "26170"); map.put("26_영도구",  "26200");
        map.put("26_부산진구","26230"); map.put("26_동래구",  "26260");
        map.put("26_남구",    "26290"); map.put("26_북구",    "26320");
        map.put("26_해운대구","26350"); map.put("26_사하구",  "26380");
        map.put("26_금정구",  "26410"); map.put("26_강서구",  "26440");
        map.put("26_연제구",  "26470"); map.put("26_수영구",  "26500");
        map.put("26_사상구",  "26530"); map.put("26_기장군",  "26710");

        // 대구
        map.put("27_중구",   "27110"); map.put("27_동구",   "27140");
        map.put("27_서구",   "27170"); map.put("27_남구",   "27200");
        map.put("27_북구",   "27230"); map.put("27_수성구", "27260");
        map.put("27_달서구", "27290"); map.put("27_달성군", "27710");
        map.put("27_군위군", "27720");

        // 인천
        map.put("28_중구",    "28110"); map.put("28_동구",    "28140");
        map.put("28_미추홀구","28177"); map.put("28_연수구",  "28185");
        map.put("28_남동구",  "28200"); map.put("28_부평구",  "28237");
        map.put("28_계양구",  "28245"); map.put("28_서구",    "28260");
        map.put("28_강화군",  "28710"); map.put("28_옹진군",  "28720");

        // 광주
        map.put("29_동구",   "29110"); map.put("29_서구",   "29140");
        map.put("29_남구",   "29155"); map.put("29_북구",   "29170");
        map.put("29_광산구", "29200");

        // 대전
        map.put("30_동구",   "30110"); map.put("30_중구",   "30140");
        map.put("30_서구",   "30170"); map.put("30_유성구", "30200");
        map.put("30_대덕구", "30230");

        // 울산
        map.put("31_중구",   "31110"); map.put("31_남구",   "31140");
        map.put("31_동구",   "31170"); map.put("31_북구",   "31200");
        map.put("31_울주군", "31710");

        // 세종
        map.put("36_세종시", "36110");

        // 경기
        map.put("41_수원시",  "41110"); map.put("41_성남시",  "41130");
        map.put("41_의정부시","41150"); map.put("41_안양시",  "41170");
        map.put("41_부천시",  "41190"); map.put("41_광명시",  "41210");
        map.put("41_평택시",  "41220"); map.put("41_동두천시","41250");
        map.put("41_안산시",  "41270"); map.put("41_고양시",  "41280");
        map.put("41_과천시",  "41290"); map.put("41_구리시",  "41310");
        map.put("41_남양주시","41360"); map.put("41_오산시",  "41370");
        map.put("41_시흥시",  "41390"); map.put("41_군포시",  "41410");
        map.put("41_의왕시",  "41430"); map.put("41_하남시",  "41450");
        map.put("41_용인시",  "41460"); map.put("41_파주시",  "41480");
        map.put("41_이천시",  "41500"); map.put("41_안성시",  "41550");
        map.put("41_김포시",  "41570"); map.put("41_화성시",  "41590");
        map.put("41_광주시",  "41610"); map.put("41_양주시",  "41630");
        map.put("41_포천시",  "41650"); map.put("41_여주시",  "41670");
        map.put("41_연천군",  "41800"); map.put("41_가평군",  "41820");
        map.put("41_양평군",  "41830");

        // 강원
        map.put("42_춘천시", "42110"); map.put("42_원주시", "42130");
        map.put("42_강릉시", "42150"); map.put("42_동해시", "42170");
        map.put("42_태백시", "42190"); map.put("42_속초시", "42210");
        map.put("42_삼척시", "42230"); map.put("42_홍천군", "42720");
        map.put("42_횡성군", "42730"); map.put("42_영월군", "42750");
        map.put("42_평창군", "42760"); map.put("42_정선군", "42770");
        map.put("42_철원군", "42780"); map.put("42_화천군", "42790");
        map.put("42_양구군", "42800"); map.put("42_인제군", "42810");
        map.put("42_고성군", "42820"); map.put("42_양양군", "42830");

        // 충북
        map.put("43_청주시", "43110"); map.put("43_충주시", "43130");
        map.put("43_제천시", "43150"); map.put("43_보은군", "43720");
        map.put("43_옥천군", "43730"); map.put("43_영동군", "43740");
        map.put("43_증평군", "43745"); map.put("43_진천군", "43750");
        map.put("43_괴산군", "43760"); map.put("43_음성군", "43770");
        map.put("43_단양군", "43800");

        // 충남
        map.put("44_천안시", "44130"); map.put("44_공주시", "44150");
        map.put("44_보령시", "44180"); map.put("44_아산시", "44200");
        map.put("44_서산시", "44210"); map.put("44_논산시", "44230");
        map.put("44_계룡시", "44250"); map.put("44_당진시", "44270");
        map.put("44_금산군", "44710"); map.put("44_부여군", "44760");
        map.put("44_서천군", "44770"); map.put("44_청양군", "44790");
        map.put("44_홍성군", "44800"); map.put("44_예산군", "44810");
        map.put("44_태안군", "44825");

        // 전북
        map.put("45_전주시", "45110"); map.put("45_군산시", "45130");
        map.put("45_익산시", "45140"); map.put("45_정읍시", "45180");
        map.put("45_남원시", "45190"); map.put("45_김제시", "45210");
        map.put("45_완주군", "45710"); map.put("45_진안군", "45720");
        map.put("45_무주군", "45730"); map.put("45_장수군", "45740");
        map.put("45_임실군", "45750"); map.put("45_순창군", "45770");
        map.put("45_고창군", "45790"); map.put("45_부안군", "45800");

        // 전남
        map.put("46_목포시", "46110"); map.put("46_여수시", "46130");
        map.put("46_순천시", "46150"); map.put("46_나주시", "46170");
        map.put("46_광양시", "46230"); map.put("46_담양군", "46710");
        map.put("46_곡성군", "46720"); map.put("46_구례군", "46730");
        map.put("46_고흥군", "46770"); map.put("46_보성군", "46780");
        map.put("46_화순군", "46790"); map.put("46_장흥군", "46800");
        map.put("46_강진군", "46810"); map.put("46_해남군", "46820");
        map.put("46_영암군", "46830"); map.put("46_무안군", "46840");
        map.put("46_함평군", "46860"); map.put("46_영광군", "46870");
        map.put("46_장성군", "46880"); map.put("46_완도군", "46890");
        map.put("46_진도군", "46900"); map.put("46_신안군", "46910");

        // 경북
        map.put("47_포항시", "47110"); map.put("47_경주시", "47130");
        map.put("47_김천시", "47150"); map.put("47_안동시", "47170");
        map.put("47_구미시", "47190"); map.put("47_영주시", "47210");
        map.put("47_영천시", "47220"); map.put("47_상주시", "47230");
        map.put("47_문경시", "47250"); map.put("47_경산시", "47290");
        map.put("47_의성군", "47730"); map.put("47_청송군", "47750");
        map.put("47_영양군", "47760"); map.put("47_영덕군", "47770");
        map.put("47_청도군", "47820"); map.put("47_고령군", "47830");
        map.put("47_성주군", "47840"); map.put("47_칠곡군", "47850");
        map.put("47_예천군", "47900"); map.put("47_봉화군", "47920");
        map.put("47_울진군", "47930"); map.put("47_울릉군", "47940");

        // 경남
        map.put("48_창원시", "48110"); map.put("48_진주시", "48170");
        map.put("48_통영시", "48220"); map.put("48_사천시", "48240");
        map.put("48_김해시", "48250"); map.put("48_밀양시", "48270");
        map.put("48_거제시", "48310"); map.put("48_양산시", "48330");
        map.put("48_의령군", "48720"); map.put("48_함안군", "48730");
        map.put("48_창녕군", "48740"); map.put("48_고성군", "48820");
        map.put("48_남해군", "48840"); map.put("48_하동군", "48850");
        map.put("48_산청군", "48860"); map.put("48_함양군", "48870");
        map.put("48_거창군", "48880"); map.put("48_합천군", "48890");

        // 제주
        map.put("50_제주시",   "50110");
        map.put("50_서귀포시", "50130");

        SIGUNGU_ZSCODE_MAP = Collections.unmodifiableMap(map);
    }

    // ─────────────────────────────────────────────
    // savePage
    // ─────────────────────────────────────────────
    @Transactional
    public void savePage(List<JsonNode> items) {
        Set<String> stationIds = new LinkedHashSet<>();
        Map<String, ChargingStation> stationMap = new LinkedHashMap<>();
        List<Charger> chargers = new ArrayList<>();

        for (JsonNode item : items) {
            if (!chargingStationValidator.isValidApiItem(item)) {
                continue;
            }

            ChargingStation station = buildStation(item);
            Charger charger = buildCharger(item, station);

            stationIds.add(station.getStationId());
            stationMap.put(station.getStationId(), station);
            chargers.add(charger);
        }

        if (stationMap.isEmpty()) {
            return;
        }

        chargingStationRepository.saveAll(stationMap.values());
        chargerRepository.deleteByStationIds(stationIds);
        chargerRepository.saveAll(chargers);
    }

    // ─────────────────────────────────────────────
    // refreshStatusPage
    // ─────────────────────────────────────────────
    @Transactional
    public void refreshStatusPage(List<JsonNode> items) {
        for (JsonNode item : items) {
            String stationId = getText(item, "statId");
            String chargerId = getText(item, "chgerId");

            if (stationId.isBlank() || chargerId.isBlank()) {
                continue;
            }

            chargerRepository.updateStatusByStationIdAndChargerId(
                    stationId,
                    chargerId,
                    getText(item, "stat"),
                    parseDateTime(getText(item, "statUpdDt"))
            );
        }
    }

    // ─────────────────────────────────────────────
    // buildStation (zcode/zscode 보정 포함)
    // ─────────────────────────────────────────────
    private ChargingStation buildStation(JsonNode item) {
        String address = getText(item, "addr");
        String zcode   = correctZcode(address, getText(item, "zcode"));
        String zscode  = correctZscode(address, getText(item, "zscode"), zcode);

        return ChargingStation.builder()
                .stationId(getText(item, "statId"))
                .stationName(getText(item, "statNm"))
                .address(address)
                .lat(getDouble(item, "lat"))
                .lng(getDouble(item, "lng"))
                .useTime(getText(item, "useTime"))
                .zcode(zcode)
                .zscode(zscode)
                .operatorName(getText(item, "busiNm"))
                .operatorCall(getText(item, "busiCall"))
                .parkingFree(getText(item, "parkingFree"))
                .note(resolveNote(item))
                .build();
    }

    // ─────────────────────────────────────────────
    // zcode/zscode 보정 메서드
    // ─────────────────────────────────────────────
    private String correctZcode(String address, String originalZcode) {
        if (address == null || address.isBlank()) {
            return originalZcode;
        }
        String sido = address.trim().split("\\s+")[0];
        return SIDO_ZCODE_MAP.getOrDefault(sido, originalZcode);
    }

    private String correctZscode(String address, String originalZscode, String zcode) {
        if (address == null || address.isBlank() || zcode == null || zcode.isBlank()) {
            return originalZscode;
        }
        String[] parts = address.trim().split("\\s+");
        if (parts.length < 2) {
            return originalZscode;
        }
        String sigungu = parts[1];
        String mapped = SIGUNGU_ZSCODE_MAP.get(zcode + "_" + sigungu);
        return mapped != null ? mapped : originalZscode;
    }

    // ─────────────────────────────────────────────
    // 나머지 기존 메서드
    // ─────────────────────────────────────────────
    private Charger buildCharger(JsonNode item, ChargingStation station) {
        return Charger.builder()
                .chargerId(getText(item, "chgerId"))
                .chargerType(getText(item, "chgerType"))
                .powerType(getText(item, "powerType"))
                .status(getText(item, "stat"))
                .statusUpdatedAt(parseDateTime(getText(item, "statUpdDt")))
                .chargingStation(station)
                .build();
    }

    private String resolveNote(JsonNode item) {
        String limitDetail = getText(item, "limitDetail");
        if (!limitDetail.isBlank()) {
            return limitDetail;
        }

        String note = getText(item, "note");
        if (!note.isBlank()) {
            return note;
        }

        return "이용 안내 없음";
    }

    private String getText(JsonNode item, String fieldName) {
        return normalize(item.path(fieldName).asText(""));
    }

    private double getDouble(JsonNode item, String fieldName) {
        String value = getText(item, fieldName);
        if (value.isBlank()) {
            return 0D;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0D;
        }
    }

    private LocalDateTime parseDateTime(String value) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(normalized, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.trim();
        if ("null".equalsIgnoreCase(normalized)) {
            return "";
        }
        return normalized;
    }
}