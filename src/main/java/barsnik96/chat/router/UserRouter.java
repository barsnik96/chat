package barsnik96.chat.router;

import barsnik96.chat.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> routeUsers(UserHandler userHandler) {
        RouterFunction<ServerResponse> userRoutes = RouterFunctions
                .route(GET("/")
                        .and(accept(APPLICATION_JSON)), userHandler::getAll)
                .andRoute(GET("/{uid}")
                        .and(accept(APPLICATION_JSON)), userHandler::get)
                .andRoute(DELETE("/{uid}"), userHandler::delete)
                .andRoute(POST("/{uid}/change_password"), userHandler::changePassword)
                .andRoute(PUT("/{uid}/roles"), userHandler::putRole)
                .andRoute(DELETE("/{uid}/roles"), userHandler::deleteRole)
                .andRoute(PUT("/{uid}/status"), userHandler::putStatus)
                .andRoute(DELETE("/{uid}/status"), userHandler::deleteStatus);

        return RouterFunctions.nest(path("/api/v1/users"), userRoutes);
    }
}
