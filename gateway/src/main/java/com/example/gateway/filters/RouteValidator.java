package com.example.gateway.filters;

import com.example.gateway.model.Endpoint;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> publicEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/validate"
    );
    //TODO fix this
//    private Set<Endpoint> adminEndpoints = new HashSet<>();
//    public Predicate<ServerHttpRequest> isAdmin = request ->
//            adminEndpoints.stream()
//                    .anyMatch(endpoint -> request.getURI()
//                            .getPath()
//                            .contains(endpoint.getUrl())
//                            && request.getMethod().name().equals(endpoint.getHttpMethod().name()));

    public Predicate<ServerHttpRequest> isSecured = request ->
            publicEndpoints.stream()
            .noneMatch(endpoint -> request.getURI()
                    .getPath()
                    .contains(endpoint));
}
