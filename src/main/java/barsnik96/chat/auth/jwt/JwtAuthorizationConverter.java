package barsnik96.chat.auth.jwt;

import barsnik96.chat.util.JwtUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

public class JwtAuthorizationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();

        Mono<String> sseToken = Mono.justOrEmpty(request.getQueryParams().getFirst("token"))
                .filter(token -> request.getHeaders().getAccept().contains(TEXT_EVENT_STREAM));

        return Mono.justOrEmpty(request.getHeaders().getFirst(AUTHORIZATION))
                .switchIfEmpty(sseToken)
                .filter(authorization -> authorization.toLowerCase().startsWith(JwtUtils.JWT_PREFIX.toLowerCase()))
                .map(authorization -> authorization.substring(JwtUtils.JWT_PREFIX.length()))
                .map(JwtUtils::verifyToken)
                .map(JwtUtils::getName)
                .map(username -> new UsernamePasswordAuthenticationToken(username, null));
    }
}
