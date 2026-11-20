package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/** Obtains the sale date from World Clock and falls back to the local Java clock. */
@Service
public class FechaComprobanteService {
    private static final Logger logger = LoggerFactory.getLogger(FechaComprobanteService.class);
    private static final URI WORLD_CLOCK_URI = URI.create("http://worldclockapi.com/api/json/utc/now");

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();

    public FechaComprobanteService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public LocalDate obtenerFecha() {
        try {
            HttpRequest request = HttpRequest.newBuilder(WORLD_CLOCK_URI)
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("World Clock respondió HTTP " + response.statusCode());
            }

            JsonNode currentDateTime = objectMapper.readTree(response.body()).get("currentDateTime");
            if (currentDateTime == null || currentDateTime.asText().isBlank()) {
                throw new IllegalStateException("World Clock no informó currentDateTime");
            }
            return parsearFecha(currentDateTime.asText());
        } catch (Exception exception) {
            logger.warn("No se pudo consultar World Clock; se usará la fecha de Java: {}", exception.getMessage());
            return new Date().toInstant().atZone(ZoneOffset.UTC).toLocalDate();
        }
    }

    private LocalDate parsearFecha(String valor) {
        try {
            return OffsetDateTime.parse(valor).toLocalDate();
        } catch (RuntimeException ignored) {
            try {
                return LocalDateTime.parse(valor).toLocalDate();
            } catch (RuntimeException ignoredAgain) {
                return LocalDate.parse(valor);
            }
        }
    }
}
