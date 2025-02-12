package com.cdurro.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtValidationFilter implements GlobalFilter {

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8081/users/validateToken";

    private String getJwtTokenFromRequest(ServerHttpRequest request) {
        // Extract the JWT token from the Authorization header
    	
    	String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    	
    	if (authHeader == null)
    		return null;
    	
        return authHeader.substring(7);
    }

    private Boolean isValidToken(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        
        try {
        
	        // Validate the token by calling the user-service
	        ResponseEntity<String> response = restTemplate.exchange(
	                USER_SERVICE_URL, HttpMethod.GET, entity, String.class
	        );
	        
	        return response.getStatusCode() == HttpStatus.OK;
    	
        }
        catch (RestClientException e) {
        	System.out.println("Error validating token: " + e.getMessage());
        	return false;
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        // Send back a 401 Unauthorized response
    	System.out.println(message);
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
    	try {
    	
	        // Get the request path
	        String path = exchange.getRequest().getURI().getPath();
	
	        // Only validate token for routes that require authentication
	        if (path.contains("/tickets") || path.contains("/schedules") || path.contains("/seats") || path.contains("/checkout") || path.contains("/profile") || path.contains("/reservation")) {
	            String token = getJwtTokenFromRequest(exchange.getRequest());
	            
	            if ((token == null && !path.contains("/schedules")) || (token != null && !isValidToken(token))) {
	                return onError(exchange, "Token expired or invalid", HttpStatus.UNAUTHORIZED);
	            }
	        }
	
	        // Continue processing the request if token is valid or no validation needed
	        return chain.filter(exchange);
    	}
    	catch (Exception e) {
    		return onError(exchange, "Exception error", HttpStatus.BAD_GATEWAY);
    	}
	}

}


