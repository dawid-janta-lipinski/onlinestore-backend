package com.example.gateway.controller;

import com.example.gateway.filters.RouteValidator;
import com.example.gateway.model.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jantalipinski.model.Response;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/gateway")
@RequiredArgsConstructor
public class GatewayController {
    private final RouteValidator routeValidator;
    @PostMapping
    public ResponseEntity<Response> register(@RequestBody List<Endpoint> endpoints){
        routeValidator.addEndpoints(endpoints);
        return ResponseEntity.ok(new Response("Successfully register new endpoints"));
    }

}
