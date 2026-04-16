package com.evcar.service.charging;

import com.evcar.config.ChargingApiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChargingStationApiService {

    private static final int INFO_PAGE_SIZE = 500;
    private static final int STATUS_PAGE_SIZE = 500;
    private static final int STATUS_PERIOD_MINUTES = 10;
    private static final int MAX_RETRY = 3;

    private final ChargingApiProperties chargingApiProperties;
    private final ChargingStationPersistenceService chargingStationPersistenceService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void loadData() {
        for (String zcode : chargingApiProperties.getZcodes()) {
            saveByZcode(zcode);
        }
    }

    public void saveByZcode(String zcode) {
        RestTemplate restTemplate = createRestTemplate();

        int pageNo = 1;
        int totalCount = Integer.MAX_VALUE;

        while ((pageNo - 1) * INFO_PAGE_SIZE < totalCount) {
            URI uri = buildInfoUri(zcode, pageNo, INFO_PAGE_SIZE);
            String body = fetchWithRetry(restTemplate, uri, pageNo);

            if (body == null || body.isBlank()) {
                break;
            }

            JsonNode root = readJson(body);
            JsonNode header = extractHeader(root);
            validateResult(header, body);

            totalCount = header.path("totalCount").asInt(0);

            List<JsonNode> items = extractItems(root);
            if (items.isEmpty()) {
                break;
            }

            chargingStationPersistenceService.savePage(items);
            pageNo++;
        }
    }

    public void refreshChargerStatus() {
        RestTemplate restTemplate = createRestTemplate();

        int pageNo = 1;
        int totalCount = Integer.MAX_VALUE;

        while ((pageNo - 1) * STATUS_PAGE_SIZE < totalCount) {
            URI uri = buildStatusUri(pageNo, STATUS_PAGE_SIZE, STATUS_PERIOD_MINUTES);
            String body = fetchWithRetry(restTemplate, uri, pageNo);

            if (body == null || body.isBlank()) {
                break;
            }

            JsonNode root = readJson(body);
            JsonNode header = extractHeader(root);
            validateResult(header, body);

            totalCount = header.path("totalCount").asInt(0);

            List<JsonNode> items = extractItems(root);
            if (items.isEmpty()) {
                break;
            }

            chargingStationPersistenceService.refreshStatusPage(items);
            pageNo++;
        }
    }

    private RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(30));
        factory.setReadTimeout(Duration.ofSeconds(60));
        return new RestTemplate(factory);
    }

    private URI buildInfoUri(String zcode, int pageNo, int pageSize) {
        return UriComponentsBuilder.fromUriString(chargingApiProperties.getBaseUrl() + "/getChargerInfo")
                .queryParam("ServiceKey", chargingApiProperties.getServiceKey())
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", pageSize)
                .queryParam("dataType", "JSON")
                .queryParam("zcode", zcode)
                .build(false)
                .toUri();
    }

    private URI buildStatusUri(int pageNo, int pageSize, int periodMinutes) {
        return UriComponentsBuilder.fromUriString(chargingApiProperties.getBaseUrl() + "/getChargerStatus")
                .queryParam("ServiceKey", chargingApiProperties.getServiceKey())
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", pageSize)
                .queryParam("dataType", "JSON")
                .queryParam("period", periodMinutes)
                .build(false)
                .toUri();
    }

    private String fetchWithRetry(RestTemplate restTemplate, URI uri, int pageNo) {
        for (int retry = 1; retry <= MAX_RETRY; retry++) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
                return response.getBody();
            } catch (Exception e) {
                if (retry == MAX_RETRY) {
                    throw e;
                }

                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("충전소 API 호출 대기 중 인터럽트 발생", interruptedException);
                }
            }
        }

        return null;
    }

    private JsonNode readJson(String body) {
        try {
            return objectMapper.readTree(body);
        } catch (Exception e) {
            throw new IllegalStateException("충전소 OpenAPI 응답 파싱 실패", e);
        }
    }

    private JsonNode extractHeader(JsonNode root) {
        if (root.has("header")) {
            return root.path("header");
        }

        if (root.has("response")) {
            return root.path("response").path("header");
        }

        return root;
    }

    private List<JsonNode> extractItems(JsonNode root) {
        List<JsonNode> result = new ArrayList<>();

        JsonNode items = root.path("items").path("item");
        if (items.isArray()) {
            items.forEach(result::add);
            return result;
        }

        if (!items.isMissingNode() && !items.isNull()) {
            result.add(items);
            return result;
        }

        JsonNode responseItems = root.path("response").path("body").path("items").path("item");
        if (responseItems.isArray()) {
            responseItems.forEach(result::add);
            return result;
        }

        if (!responseItems.isMissingNode() && !responseItems.isNull()) {
            result.add(responseItems);
        }

        return result;
    }

    private void validateResult(JsonNode header, String body) {
        String resultCode = header.path("resultCode").asText("");
        String resultMsg = header.path("resultMsg").asText("");

        if (!"00".equals(resultCode)) {
            throw new IllegalStateException(
                    "충전소 OpenAPI 호출 실패: " + resultCode + " / " + resultMsg + " / body=" + body
            );
        }
    }
}