package barsnik96.chat.repository;

import barsnik96.chat.model.Chat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatRepository extends ReactiveCrudRepository<Chat, String> {

    Flux<Chat> findAllByMembers(String userId);
}
