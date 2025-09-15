package com.portfolio.common.business.api;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HolidayApiClient {

    private final RestTemplate restTemplate;

    @Value("${api.holiday.service-key}")
    private String serviceKey;

    private static final String API_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo";

    public HolidayApiClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Set<LocalDate> getHoliday(int year) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(API_URL)
                .queryParam("serviceKey", this.serviceKey)
                .queryParam("solYear", year)
                .queryParam("numOfRows", "100");

        URI uri = uriBuilder.build().toUri();

//        log.info("Requesting holidays for year {} from API: {}", year, uri);

        try {
            // 3. RestTemplate을 사용하여 GET 요청을 보내고, 응답을 ApiResponse 객체로 받습니다.
            //    이 한 줄이 샘플 코드의 HttpURLConnection, 스트림 읽기 등의 모든 과정을 대체합니다.
            ApiResponse response = restTemplate.getForObject(uri, ApiResponse.class);

            if (response != null && response.body != null && response.body.items != null && response.body.items.itemList != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

                return response.body.items.itemList.stream()
                        .map(item -> LocalDate.parse(String.valueOf(item.locdate), formatter))
                        .collect(Collectors.toSet());
            }
        } catch (RestClientException e) {
            log.error("Failed to fetch holidays for year: {} from API. Error: {}", year, e.getMessage());
        }

        return Collections.emptySet();
    }

    // --- JAXB를 사용하여 XML 응답을 매핑할 DTO 클래스들 ---
    @XmlRootElement(name = "response")
    public static class ApiResponse {
        @XmlElement(name = "body") Body body;
    }
    public static class Body {
        @XmlElement(name = "items") Items items;
    }
    public static class Items {
        @XmlElement(name = "item")
        List<Item> itemList;
    }
    public static class Item {
        @XmlElement(name = "locdate") int locdate;
        @XmlElement(name = "dateName") String dateName;
    }
}
