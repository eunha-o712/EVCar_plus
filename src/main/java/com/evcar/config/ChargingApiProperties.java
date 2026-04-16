package com.evcar.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ev.public-api.evcharger")
public class ChargingApiProperties {

    private String serviceKey;
    private String baseUrl;
    private List<String> zcodes = new ArrayList<>();
}