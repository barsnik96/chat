package barsnik96.chat.router;

import barsnik96.chat.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
public class AuthRouter {

    @Bean
    public RouterFunction<ServerResponse> routeAuth(AuthHandler authHandler) {
        RouterFunction<ServerResponse> authRoutes = RouterFunctions
                .route(POST("/signup"), authHandler::signup)
                .andRoute(POST("/signin"), authHandler::signin);

        return RouterFunctions.nest(path("/auth"), authRoutes);
    }
}
