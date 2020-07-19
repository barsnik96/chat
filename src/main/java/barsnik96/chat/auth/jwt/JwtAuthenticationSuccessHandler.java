package barsnik96.chat.auth.jwt;

import barsnik96.chat.util.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();

        exchange.getResponse().getHeaders()
                .add(AUTHORIZATION, JwtUtils.JWT_PREFIX + JwtUtils.createToken(authentication));

        return webFilterExchange.getChain().filter(exchange);
    }
}
