package barsnik96.chat.handler;

import barsnik96.chat.service.UserService;
import barsnik96.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class AuthHandler {

    private final UserService userService;

    @Autowired
    public AuthHandler(UserService userService) {
        this.userService = userService;
    }

    public Mono<ServerResponse> signup(ServerRequest request) {
        return request
                .bodyToMono(User.class)
                .flatMap(userService::save)
                .flatMap(user -> ServerResponse.created(URI.create("/api/v1/users/" + user.getId())).build());
    }

    public Mono<ServerResponse> signin(ServerRequest request) {
        return ServerResponse.ok().build();
    }
}
