package com.example.file.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pl.jantalipinski.ApiGatewayEndpointConfiguration;
import pl.jantalipinski.model.Endpoint;
import pl.jantalipinski.model.HttpMethod;
import pl.jantalipinski.model.Response;
import pl.jantalipinski.model.Role;

@Configuration
public class ApiGatewayConfiguration implements ApiGatewayEndpointConfiguration {
    @Value("${api-gateway.url}")
    private String GATEWAY_URL;

    @PostConstruct
    public void startOperation(){
        initMap();
        register();
    }

    @Override
    public void initMap() {
        endpointList.add(new Endpoint("/api/v1/image", HttpMethod.GET, Role.GUEST));
        endpointList.add(new Endpoint("/api/v1/image", HttpMethod.POST, Role.GUEST));
        endpointList.add(new Endpoint("/api/v1/image", HttpMethod.PATCH, Role.GUEST));
        endpointList.add(new Endpoint("/api/v1/image", HttpMethod.DELETE, Role.ADMIN));
    }

    @Override
    public void register() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Response> response = restTemplate.postForEntity(GATEWAY_URL, endpointList, Response.class);
        if (response.getStatusCode().isError()) throw new RuntimeException();
    }

}
